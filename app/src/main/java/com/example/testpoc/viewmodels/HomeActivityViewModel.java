package com.example.testpoc.viewmodels;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.clj.fastble.data.BleDevice;
import com.example.testpoc.models.BLE;
import com.example.testpoc.utils.BleConnectionStatus;
import com.example.testpoc.utils.Device;
import com.orhanobut.logger.Logger;
import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityViewModel extends AndroidViewModel {

    BLE bleObj = new BLE();

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> counter = new MutableLiveData(0);

    public MutableLiveData<Boolean> isScanningForDevices = new MutableLiveData(false);
    public MutableLiveData<List<Device>> listOfDiscoveredDevices = new MutableLiveData(new ArrayList());

    public MutableLiveData<String> bleErrorMessage = new MutableLiveData<>("");

    public void incrementCounter() {
        counter.postValue(counter.getValue() + 1);
    }

    public void decrementCounter() {
        counter.postValue(counter.getValue() - 1);
    }

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
                        Logger.d("All permissions are granted. Proceeding to checking BLE state");
                        checkBleState();
                    } else {
                        Logger.d("These permissions are denied: " + deniedList);
                        bleErrorMessage.postValue("Insufficient permissions");
                    }
                });
    }

    private void checkBleState() {
        boolean result = bleObj.bleManager.isBlueEnable();
        if (!result) {
            bleErrorMessage.postValue("BLE is turned off");
        } else {
            Logger.d("Ble state OK. Proceeding to check location state...");
            checkLocationState();
        }
    }

    private void checkLocationState() {
        LocationManager locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        boolean result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!result) {
            bleErrorMessage.postValue("Location not turned on");
        } else {
            Logger.d("Agnesh | BLE Fragment | HomeActivityViewModel -> Permissions and hardware ok...so scanning...");
            startScanning();
        }
    }

    public void initializeBle() {
        bleObj.initBle(getApplication());
    }

    public void startScanning() {
        isScanningForDevices.postValue(true);
        bleObj.AttemptBleScan().thenAccept(discoveredDevices -> {
            List<Device> deviceList = new ArrayList<>();
            for (BleDevice device : discoveredDevices) {
                Device data = new Device(device, device.getMac(), device.getName(), new MutableLiveData(BleConnectionStatus.DISCONNECTED), device.getRssi(), device.getScanRecord());
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

    public void connectDevice(Device bleDevice) {
        bleObj.connectBleDevice(bleDevice, bleErrorMessage);
    }

    public void disconnectDevice(Device bleDevice) {
        bleObj.disconnectBleDevice(bleDevice.getBleDevice());
    }

    public void readData(BleDevice bleDevice) {
        bleObj.readPressureData(bleDevice, bleErrorMessage);
    }

}
