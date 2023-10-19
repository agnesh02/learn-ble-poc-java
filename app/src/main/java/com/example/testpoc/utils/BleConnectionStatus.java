package com.example.testpoc.utils;

public enum BleConnectionStatus {

    DISCONNECTED("Connect",false),
    CONNECTED("Disconnect", true),
    CONNECTING("Connecting...",false);

    private final String actionMessage;
    private final Boolean connectionStatus;

    BleConnectionStatus(String actionMessage, Boolean connectionStatus) {
        this.actionMessage = actionMessage;
        this.connectionStatus = connectionStatus;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public Boolean getConnectionStatus() {
        return connectionStatus;
    }
}
