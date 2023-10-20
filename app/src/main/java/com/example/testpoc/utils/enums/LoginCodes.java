package com.example.testpoc.utils.enums;

public enum LoginCodes {

    SUCCESS("OK"),
    USERNAME_SHORT("Username should be minimum of 4 characters"),
    PASSWORD_SHORT("Password should have a minimum of 6 characters");

    private final String value;

    LoginCodes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
