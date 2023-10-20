package com.example.testpoc.models;

import android.app.Application;
import android.bluetooth.BluetoothGatt;

import androidx.lifecycle.MutableLiveData;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.example.testpoc.utils.enums.BleConnectionStatus;
import com.example.testpoc.utils.enums.BleFeedbacks;
import com.example.testpoc.utils.enums.BleUUIDS;
import com.example.testpoc.utils.Device;
import com.orhanobut.logger.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BLE {

    private static BLE instance;
    public BleManager bleManager;

    /**
     * Singleton for this class
     *
     * @return Instance of BLE class
     */
    public static BLE getInstance() {
        if (instance == null) {
            instance = new BLE();
        }
        return instance;
    }

    /**
     * Method to initialize BLE Manager for further use in app
     *
     * @param application {@link Application} To initialize the Blemanager instance
     */
    public void initBle(Application application) {
        bleManager = BleManager.getInstance();
        bleManager.init(application);
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(6000)
                .build();
        bleManager.initScanRule(scanRuleConfig);
    }

    /**
     * Method to initiate BLE scan process.
     * Once scan is finished the list of discovered devices are returned
     *
     * @return {@link List<BleDevice>}
     */
    public CompletableFuture<List<BleDevice>> AttemptBleScan() {

        final CompletableFuture<List<BleDevice>> listOfScannedDevices = new CompletableFuture<>();

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Logger.d("Agnesh: BLE | Scan started -> " + success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Logger.d("Agnesh: BLE | Is Scanning ... -> " + Arrays.toString(bleDevice.getScanRecord()));
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Logger.d("Agnesh: BLE | Scan Finished !!");
                listOfScannedDevices.complete(scanResultList);
            }
        });

        return listOfScannedDevices;
    }

    /**
     * Method to connect with a BLE device.
     *
     * @param bleDeviceItem      {@link Device} To use for connect method
     * @param bleFeedbackMessage {@link MutableLiveData<String>} To handle responses
     */
    public void connectBleDevice(Device bleDeviceItem, MutableLiveData<String> bleFeedbackMessage) {

        bleManager.connect(bleDeviceItem.getMacAddress(), new BleGattCallback() {

            final String deviceName = bleDeviceItem.getDeviceName().getValue();

            @Override
            public void onStartConnect() {
                Logger.d("Agnesh: BLE | Connection attempt started for " + deviceName);
                bleDeviceItem.setIsConnected(BleConnectionStatus.CONNECTING);
                bleFeedbackMessage.postValue(BleFeedbacks.CONNECT_IN_PROGRESS.getBleFeedbackMessage());
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                Logger.d("Agnesh: BLE | Connection failed for " + deviceName + "-> Reason: " + exception.getDescription());
                bleFeedbackMessage.postValue("BLE connect failed for " + bleDevice.getName());
                bleFeedbackMessage.postValue(BleFeedbacks.CONNECT_FAILED.getBleFeedbackMessage());
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                Logger.d("Agnesh: BLE | Connection success for " + deviceName);
                bleDeviceItem.setIsConnected(BleConnectionStatus.CONNECTED);
                bleFeedbackMessage.postValue(BleFeedbacks.CONNECT_SUCCESS.getBleFeedbackMessage());
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                Logger.d("Agnesh: BLE | " + deviceName + " has been disconnected !");
                bleDeviceItem.setIsConnected(BleConnectionStatus.DISCONNECTED);
                bleFeedbackMessage.postValue(BleFeedbacks.DISCONNECTED.getBleFeedbackMessage());
            }
        });
    }

    /**
     * Method to terminate connection with the BLE device
     *
     * @param bleDevice {@link BleDevice} To use for disconnect method
     */
    public void disconnectBleDevice(BleDevice bleDevice) {
        bleManager.disconnect(bleDevice);
    }

    /**
     * Method to obtain readable pressure data from a Kraken
     *
     * @param bleDevice          {@link BleDevice} To use for read method
     * @param bleFeedbackMessage {@link MutableLiveData<String>} To handle responses
     */
    public void readPressureData(BleDevice bleDevice, MutableLiveData<String> bleFeedbackMessage) {

        CompletableFuture<String> fetchedData = new CompletableFuture<>();

        bleManager.read(bleDevice, BleUUIDS.KRAKEN_UUID.getUuid(), BleUUIDS.PRESSURE_UUID.getUuid(), new BleReadCallback() {
            @Override
            public void onReadSuccess(byte[] data) {
                String decodedString = new String(data);
                Logger.d("Agnesh: BLE | Read success " + bleDevice.getName() + "-> " + decodedString);
                fetchedData.complete(decodedString);
                bleFeedbackMessage.postValue(BleFeedbacks.READ_SUCCESS.getBleFeedbackMessage());
            }

            @Override
            public void onReadFailure(BleException exception) {
                Logger.d("Agnesh: BLE | Read failed for " + bleDevice.getName() + ". Reason -> " + exception.getDescription());
                fetchedData.complete("-1");
                bleFeedbackMessage.postValue(BleFeedbacks.READ_FAILED.getBleFeedbackMessage());
            }
        });

    }

    /**
     * Method to write a new device name to a Kraken
     *
     * @param device             {@link Device} To use for write method
     * @param newName            The desired name which should be written to the device
     * @param bleFeedbackMessage {@link MutableLiveData<String>} To handle responses
     */
    public void writeDeviceName(Device device, String newName, MutableLiveData<String> bleFeedbackMessage) {

        CompletableFuture<Boolean> isWriteSuccess = new CompletableFuture<>();

        byte[] encodedData;

        encodedData = newName.getBytes(StandardCharsets.UTF_8);

        bleManager.write(device.getBleDevice(), BleUUIDS.KRAKEN_UUID.getUuid(), BleUUIDS.DEVICE_NAME_UUID.getUuid(), encodedData, new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                Logger.d("Agnesh: BLE | Write success " + device.getDeviceName() + " name changed to -> " + newName);
                bleFeedbackMessage.postValue(BleFeedbacks.WRITE_SUCCESS.getBleFeedbackMessage());
                device.setDeviceName(newName);
                isWriteSuccess.complete(true);
            }

            @Override
            public void onWriteFailure(BleException exception) {
                Logger.d("Agnesh: BLE | Write failed " + exception.getDescription());
                bleFeedbackMessage.postValue(BleFeedbacks.WRITE_FAILED.getBleFeedbackMessage());
                isWriteSuccess.complete(true);
            }
        });

    }

}
