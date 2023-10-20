package com.example.testpoc.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testpoc.utils.enums.LoginCodes;
import com.orhanobut.logger.Logger;

public class LoginActivityViewModel extends AndroidViewModel {
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Live data which holds the username value - used with data binding
     */
    public MutableLiveData<String> username = new MutableLiveData<>("");

    /**
     * Live data which holds the password value - used with data binding
     */
    public MutableLiveData<String> password = new MutableLiveData<>("");

    /**
     * Live data which holds the error messages
     */
    public MutableLiveData<String> errorStatus = new MutableLiveData<>("");

    /**
     * Method to obtain the value from login fields and validate it and login
     */
    public void onLogin() {
        String uname = username.getValue().trim();
        String pass = password.getValue().trim();
        Logger.i("Agnesh: LoginViewModel | Login attempt -> username -> " + uname + " | password -> " + pass);
        validateFields(uname, pass);
    }

    /**
     * Method to validate the login fields
     *
     * @param uname {@link String} Entered username
     * @param pass  {@link String} Entered password
     */
    private void validateFields(String uname, String pass) {
        if (uname.length() < 4) {
            errorStatus.postValue(LoginCodes.USERNAME_SHORT.getValue());
        } else if (pass.length() < 6) {
            errorStatus.postValue(LoginCodes.PASSWORD_SHORT.getValue());
        } else {
            errorStatus.postValue(LoginCodes.SUCCESS.getValue());
        }
    }
}
