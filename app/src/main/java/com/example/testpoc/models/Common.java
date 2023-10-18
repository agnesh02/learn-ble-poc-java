package com.example.testpoc.models;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class Common {
    private static Common instance;

    private Common() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }


    public static Common getInstance() {
        if (instance == null) {
            instance = new Common();
        }
        return instance;
    }

    public void showMessage(Context applicationContext, String message){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
    }

}
