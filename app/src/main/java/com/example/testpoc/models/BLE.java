package com.example.testpoc.models;

import android.app.Application;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.orhanobut.logger.Logger;

import java.util.List;

public class BLE {

    private static BLE instance;
    private BleManager bleManager;

    public static BLE getInstance(){
        if(instance == null){
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

    public void AttemptBleScan(){
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Logger.d("Agnesh: BLE | Scan started -> "+success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Logger.d("Agnesh: BLE | Is Scanning ... -> "+bleDevice);
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Logger.d("Agnesh: BLE | Scan Finished -> "+scanResultList);
            }
        });
    }

}
