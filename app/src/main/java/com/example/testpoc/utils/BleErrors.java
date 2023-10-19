package com.example.testpoc.utils;

public enum BleErrors {

    BLE_OFF("Bluetooth is turned off"),
    LOCATION_OFF("Location is turned off");

    private final String BleErrorMsg;

    BleErrors(String bleErrorMsg) {
        this.BleErrorMsg = bleErrorMsg;
    }

    public String getBleErrorMsg() {
        return BleErrorMsg;
    }
}
