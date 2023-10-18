package com.example.testpoc.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testpoc.models.LoginCodes;
import com.orhanobut.logger.Logger;

public class LoginActivityViewModel extends AndroidViewModel {
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> username = new MutableLiveData("");
    public MutableLiveData<String> password = new MutableLiveData("");

    public  MutableLiveData<String> errorStatus = new MutableLiveData("");

    public void onLogin() {
        String uname = username.getValue().trim();
        String pass = password.getValue().trim();
        Logger.i("Agnesh: LoginViewModel | Login attempt -> username -> " + uname + " | password -> " + pass);
        validateFields(uname, pass);
    }

    private void validateFields(String uname, String pass){
        if(uname.length() < 4){
            errorStatus.postValue(LoginCodes.USERNAME_SHORT.getValue());
        }
        else if(pass.length() < 6){
            errorStatus.postValue(LoginCodes.PASSWORD_SHORT.getValue());
        }
        else{
            errorStatus.postValue(LoginCodes.SUCCESS.getValue());
        }
    }
}
