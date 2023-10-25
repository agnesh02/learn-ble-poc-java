package com.example.testpoc.views.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.testpoc.databinding.ActivityLoginBinding;
import com.example.testpoc.models.Common;
import com.example.testpoc.utils.enums.LoginCodes;
import com.example.testpoc.viewmodels.LoginActivityViewModel;
import com.orhanobut.logger.Logger;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        // For data binding
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        getSupportActionBar().hide();

        Common obj = Common.getInstance();
        String loginMsg;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            loginMsg = bundle.getString("loginMessage");
        } else {
            loginMsg = "Some error occurred.";
        }

        obj.showSnackMessage(binding.getRoot(), loginMsg, false);

        binding.btnLogin.setOnClickListener(view -> {
            viewModel.onLogin();
            binding.btnLogin.setEnabled(false);
            binding.progressBar.setVisibility(View.VISIBLE);
        });

        viewModel.errorStatus.observe(this, s -> {
            if (s.equals(LoginCodes.SUCCESS.getValue())) {

                boolean shouldBeRemembered = binding.checkboxRememberMe.isChecked();
                if (shouldBeRemembered) {
                    SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.testpoc", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean("isAlreadyKnown", true).apply();
                    sharedPreferences.edit().putString("username", viewModel.username.getValue()).apply();
                }
                navigateToHomeScreen(viewModel.username.getValue());
            } else if (!s.equals("")) {
                Common.getInstance().showSnackMessage(getCurrentFocus(), s, false);
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.btnLogin.setEnabled(true);
            }
        });
    }

    /**
     * Method to navigate the user to the home screen
     */
    public void navigateToHomeScreen(String uname) {
        new Handler().postDelayed(() -> {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.putExtra("loginMessage", "Hello, " + uname + ".");
            startActivity(i);
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnLogin.setEnabled(true);
        }, 1300);
    }
}
