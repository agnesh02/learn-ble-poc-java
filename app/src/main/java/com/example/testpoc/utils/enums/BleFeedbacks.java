package com.example.testpoc.utils.enums;

public enum BleFeedbacks {

    BLE_OFF("Please turn on your bluetooth"),
    LOCATION_OFF("Please turn on your location"),
    CONNECT_IN_PROGRESS("Connecting..."),
    CONNECT_FAILED("Connection failed. Please try again."),
    CONNECT_SUCCESS("Connection success."),
    READ_FAILED("Read failed. Please retry."),
    READ_SUCCESS("Read success."),
    WRITE_FAILED("Write failed. Please retry."),
    WRITE_SUCCESS("Write success."),
    INSUFFICIENT_PERMISSIONS("Please make sure you have allowed all the necessary permissions for the app."),
    DISCONNECTED("Device has been disconnected !");

    private final String BleFeedbackMessage;

    BleFeedbacks(String bleFeedbackMessage) {
        this.BleFeedbackMessage = bleFeedbackMessage;
    }

    public String getBleFeedbackMessage() {
        return BleFeedbackMessage;
    }
}
