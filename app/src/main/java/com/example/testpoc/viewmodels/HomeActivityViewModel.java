package com.example.testpoc.viewmodels;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.clj.fastble.data.BleDevice;
import com.example.testpoc.models.BLE;
import com.example.testpoc.utils.Device;
import com.example.testpoc.utils.enums.BleConnectionStatus;
import com.example.testpoc.utils.enums.BleFeedbacks;
import com.orhanobut.logger.Logger;
import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityViewModel extends AndroidViewModel {

    BLE bleObj = BLE.getInstance();

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Live data holding the counter value for counter screen - used with data binding
     */
    public MutableLiveData<Integer> counter = new MutableLiveData<>(0);

    /**
     * Live data which represents if the app is currently scanning for BLE or not
     */
    public MutableLiveData<Boolean> isScanningForDevices = new MutableLiveData<>(false);

    /**
     * Live data which holds the list of discovered BLE devices upon scanning
     */
    public MutableLiveData<List<Device>> listOfDiscoveredDevices = new MutableLiveData<>(new ArrayList<>());

    /**
     * Live data which holds the feedback messages of BLE actions. This can hold errors as well as success
     */
    public MutableLiveData<String> bleFeedbackMessage = new MutableLiveData<>("");

    /**
     * Live data which holds the feedback messages of BLE actions. This can hold errors as well as success
     */
    public MutableLiveData<Boolean> heartRateSensorConnectivity = new MutableLiveData<>(false);

    /**
     * Live data which holds the feedback messages of BLE actions. This can hold errors as well as success
     */
    public MutableLiveData<String> heartRate = new MutableLiveData<>("0");

    /**
     * Live data which holds the heart rate sensor device details
     */
    public MutableLiveData<BleDevice> heartRateDevice = new MutableLiveData<>();

    /**
     * Live data which holds the connectivity status messages of the heart rate sensor
     */
    public MutableLiveData<String> heartRateDeviceConnectivityMessages = new MutableLiveData<>();

    /**
     * Method to increment the counter value and update it
     */
    public void incrementCounter() {
        counter.postValue(counter.getValue() + 1);
    }

    /**
     * Method to decrement the counter value and update it
     */
    public void decrementCounter() {
        counter.postValue(counter.getValue() - 1);
    }

    /**
     * Method to request the necessary permissions for the app
     * Starts BLE scan every checks are ok
     *
     * @param activity {@link Activity} Used to pass to initialize {@link PermissionX}
     */
    public void requestPermissions(Activity activity) {
        PermissionX.init((FragmentActivity) activity)
                .permissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.POST_NOTIFICATIONS
                )
                .onExplainRequestReason((scope, deniedList) -> {
                    scope.showRequestReasonDialog(
                            deniedList,
                            "This app requires these permissions in order to operate correctly",
                            "OK",
                            "Cancel"
                    );
                })
                .onForwardToSettings((scope, deniedList) -> {
                    scope.showForwardToSettingsDialog(
                            deniedList,
                            "You need to allow necessary permissions in Settings manually",
                            "OK",
                            "Cancel"
                    );
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        Logger.d("Agnesh | BLE Fragment | HomeActivityViewModel -> All permissions are granted. Proceeding to checking BLE state");
                        checkBleState();
                    } else {
                        Logger.d("Agnesh | BLE Fragment | HomeActivityViewModel -> These permissions are denied: " + deniedList);
                        bleFeedbackMessage.postValue(BleFeedbacks.INSUFFICIENT_PERMISSIONS.getBleFeedbackMessage());
                    }
                });
    }

    /**
     * Method to check the current bluetooth hardware state of the mobile device
     */
    private void checkBleState() {
        boolean result = bleObj.bleManager.isBlueEnable();
        if (!result) {
            bleFeedbackMessage.postValue(BleFeedbacks.BLE_OFF.getBleFeedbackMessage());
        } else {
            Logger.d("Agnesh | BLE Fragment | HomeActivityViewModel -> Ble state OK. Proceeding to check location state...");
            checkLocationState();
        }
    }

    /**
     * Method to check the current location hardware state of the mobile device
     * Starts BLE scan if location is on
     */
    private void checkLocationState() {
        LocationManager locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        boolean result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!result) {
            bleFeedbackMessage.postValue(BleFeedbacks.LOCATION_OFF.getBleFeedbackMessage());
        } else {
            Logger.d("Agnesh | BLE Fragment | HomeActivityViewModel -> Permissions and hardware ok...so scanning...");
            startScanning();
        }
    }

    /**
     * Method to initialize BLE
     */
    public void initializeBle() {
        bleObj.initBle(getApplication());
    }

    /**
     * Method to start BLE scan process
     */
    public void startScanning() {
        isScanningForDevices.postValue(true);
        bleObj.AttemptBleScan().thenAccept(discoveredDevices -> {
            List<Device> deviceList = new ArrayList<>();
            for (BleDevice device : discoveredDevices) {
                Device data = new Device(device, device.getMac(), new MutableLiveData<>(device.getName()), new MutableLiveData<>(BleConnectionStatus.DISCONNECTED), device.getRssi(), device.getScanRecord());
                deviceList.add(data);
            }
            listOfDiscoveredDevices.postValue(deviceList);
            isScanningForDevices.postValue(false);
        }).exceptionally(e -> {
            Logger.e("Agnesh | BLE Fragment | HomeActivityViewModel : An error occurred - " + e.getMessage());
            isScanningForDevices.postValue(false);
            return null;
        });
    }

    /**
     * Method to connect with a BLE device
     *
     * @param bleDevice {@link Device} Used to pass to a method inside {@link BLE}
     */
    public void connectDevice(Device bleDevice) {
        bleObj.connectBleDevice(bleDevice, bleFeedbackMessage);
    }

    /**
     * Method to terminate connection with a BLE device
     *
     * @param bleDevice {@link Device} Used to pass to a method inside {@link BLE}
     */
    public void disconnectDevice(Device bleDevice) {
        bleObj.disconnectBleDevice(bleDevice.getBleDevice());
    }

    /**
     * Method to read data from a BLE device
     *
     * @param bleDevice {@link BleDevice} Used to pass to a method inside {@link BLE}
     */
    public void readData(BleDevice bleDevice) {
        bleObj.readPressureData(bleDevice, bleFeedbackMessage);
    }

    /**
     * Method to write data to a BLE device
     *
     * @param device        {@link Device} Used to pass to a method inside {@link BLE}
     * @param newDeviceName {@link String} Holds the desired new device name which should be written
     */
    public void writeData(Device device, String newDeviceName) {
        bleObj.writeDeviceName(device, newDeviceName, bleFeedbackMessage);
    }

    /**
     * Method to connect with the heart rate device
     *
     * @param macAddress Mac-Address of the sensor
     */
    public void connectHeartRateSensor(String macAddress) {
        bleObj.connectHeartRateSensor(macAddress, heartRateDevice, heartRateSensorConnectivity, heartRateDeviceConnectivityMessages);
    }

    /**
     * Method to start notify / start monitoring the heart rate
     */
    public void startMonitoringHeartRate() {
        bleObj.startNotifyingHeartRate(heartRateDevice.getValue(), heartRate, heartRateDeviceConnectivityMessages);
    }

    /**
     * Method to stop notify / stop monitoring the heart rate
     */
    public void stopMonitoringHeartRate() {
        bleObj.stopNotifyingHeartRate(heartRateDevice.getValue(), heartRateDeviceConnectivityMessages);
    }

    /**
     * Method to connect with the heart rate device
     */
    public void disconnectHeartRateSensor() {
        bleObj.bleManager.disconnect(heartRateDevice.getValue());
    }

    public SQLiteDatabase initializeDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getApplication().openOrCreateDatabase("HEART_RATE_DATA", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS heart_rates (timestamp VARCHAR, heart_rate VARCHAR)");
        return sqLiteDatabase;
    }

    public void insertHeartRateRecord(SQLiteDatabase database, String timestamp, String heartRate) {
        String query = String.format("INSERT INTO heart_rates (timestamp, heart_rate) VALUES ('%s', '%s')", timestamp, heartRate);
        database.execSQL(query);
    }

    public void getHeartRateRecords(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("SELECT * FROM heart_rates", null);
        int timestampIndex = cursor.getColumnIndex("timestamp");
        int heartRateIndex = cursor.getColumnIndex("heart_rate");

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Logger.i("Timestamp: "+cursor.getString(timestampIndex)+" | "+"Heart Rate: "+cursor.getString(heartRateIndex));
            cursor.moveToNext();
        }
    }

}
