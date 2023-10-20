package com.example.testpoc.utils;

import androidx.lifecycle.MutableLiveData;

import com.clj.fastble.data.BleDevice;
import com.example.testpoc.utils.enums.BleConnectionStatus;

public class Device {
    BleDevice bleDevice;
    String macAddress;
    MutableLiveData<String> deviceName;
    MutableLiveData<BleConnectionStatus> isConnected;
    int rssi;
    byte[] manufacturerData;

    public Device() {
    }

    public Device(BleDevice bleDevice, String macAddress, MutableLiveData<String> deviceName, MutableLiveData<BleConnectionStatus> isConnected, int rssi, byte[] manufacturerData) {
        this.bleDevice = bleDevice;
        this.macAddress = macAddress;
        this.deviceName = deviceName;
        this.isConnected = isConnected;
        this.rssi = rssi;
        this.manufacturerData = manufacturerData;
    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName.postValue(deviceName);
    }

    public void setIsConnected(BleConnectionStatus isConnected) {
        this.isConnected.postValue(isConnected);
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setManufacturerData(byte[] manufacturerData) {
        this.manufacturerData = manufacturerData;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public MutableLiveData<String> getDeviceName() {
        return deviceName;
    }

    public MutableLiveData<BleConnectionStatus> getIsConnected() {
        return isConnected;
    }

    public int getRssi() {
        return rssi;
    }

    public byte[] getManufacturerData() {
        return manufacturerData;
    }
}
