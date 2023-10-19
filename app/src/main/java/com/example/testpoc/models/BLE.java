package com.example.testpoc.models;

import android.app.Application;
import android.bluetooth.BluetoothGatt;

import androidx.lifecycle.MutableLiveData;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.example.testpoc.utils.BleConnectionStatus;
import com.example.testpoc.utils.BleUUIDS;
import com.example.testpoc.utils.Device;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BLE {

    private static BLE instance;
    public BleManager bleManager;

    public static BLE getInstance() {
        if (instance == null) {
            instance = new BLE();
        }
        return instance;
    }

    public void initBle(Application application) {
        bleManager = BleManager.getInstance();
        bleManager.init(application);
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(6000)
                .build();
        bleManager.initScanRule(scanRuleConfig);
    }

    public CompletableFuture<List<BleDevice>> AttemptBleScan() {

        final CompletableFuture<List<BleDevice>> listOfScannedDevices = new CompletableFuture();

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Logger.d("Agnesh: BLE | Scan started -> " + success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Logger.d("Agnesh: BLE | Is Scanning ... -> " + bleDevice.getScanRecord());
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Logger.d("Agnesh: BLE | Scan Finished !!");
                listOfScannedDevices.complete(scanResultList);
            }
        });

        return listOfScannedDevices;
    }

    public void connectBleDevice(Device bleDeviceItem, MutableLiveData<String> bleErrorMsg) {
        bleManager.connect(bleDeviceItem.getMacAddress(), new BleGattCallback() {

            String deviceName = bleDeviceItem.getDeviceName();

            @Override
            public void onStartConnect() {
                Logger.d("Agnesh: BLE | Connection attempt started for " + deviceName);
                bleDeviceItem.setIsConnected(BleConnectionStatus.CONNECTING);
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                Logger.d("Agnesh: BLE | Connection failed for " + deviceName + "-> Reason: " + exception.getDescription());
                bleErrorMsg.postValue("BLE connect failed for "+bleDevice.getName());
//                bleDeviceItem.setIsConnected(BleConnectionStatus.DISCONNECTED);
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                Logger.d("Agnesh: BLE | Connection success for " + deviceName);
                bleDeviceItem.setIsConnected(BleConnectionStatus.CONNECTED);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                Logger.d("Agnesh: BLE | " + deviceName + " has been disconnected !");
                bleDeviceItem.setIsConnected(BleConnectionStatus.DISCONNECTED);
            }
        });
    }

    public void disconnectBleDevice(BleDevice bleDevice) {
        bleManager.disconnect(bleDevice);
    }

    public CompletableFuture<String> readPressureData(BleDevice bleDevice, MutableLiveData<String> bleErrorMsg) {

        CompletableFuture<String> fetchedData = new CompletableFuture<>();

        bleManager.read(bleDevice, BleUUIDS.KRAKEN_UUID.getUuid(), BleUUIDS.PRESSURE_UUID.getUuid(), new BleReadCallback() {
            @Override
            public void onReadSuccess(byte[] data) {
                String decodedString = new String(data);
                Logger.d("Agnesh: BLE | Read success " + bleDevice.getName() + "-> " + decodedString);
                fetchedData.complete(decodedString);
            }

            @Override
            public void onReadFailure(BleException exception) {
                Logger.d("Agnesh: BLE | Read failed for " + bleDevice.getName() + ". Reason -> " + exception.getDescription());
                fetchedData.complete("-1");
                bleErrorMsg.postValue("BLE read failed for "+bleDevice.getName());
            }
        });

        return fetchedData;
    }

}
