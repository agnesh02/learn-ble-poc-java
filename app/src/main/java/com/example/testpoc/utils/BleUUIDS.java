package com.example.testpoc.utils;

public enum BleUUIDS {

    KRAKEN_UUID("7db25886-88c8-495c-a451-8e43d5e8e7d0"),
    PRESSURE_UUID("324d6d26-a1f6-4a87-8513-0cffa5623c15"),
    HEART_RATE_UUID("0000180d-0000-1000-8000-00805f9b34fb"),
    HEART_RATE_MEASUREMENT("00002a37-0000-1000-8000-00805f9b34fb");

    private final String uuid;

    BleUUIDS(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
