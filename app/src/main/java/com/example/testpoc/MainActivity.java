package com.example.testpoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testpoc.views.activities.HomeActivity;
import com.example.testpoc.views.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.testpoc", Context.MODE_PRIVATE);
        boolean isAlreadyKnown = sharedPreferences.getBoolean("isAlreadyKnown", false);
        String uname = sharedPreferences.getString("username", "");

        // Splash screen
        new Handler().postDelayed(() -> {
            Intent intent;
            if (isAlreadyKnown) {
                intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("loginMessage", "Welcome back, " + uname + ".");
            } else {
                intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("loginMessage", "Please login.");
            }
            startActivity(intent);
            this.finish();
        }, 2000);
    }
}