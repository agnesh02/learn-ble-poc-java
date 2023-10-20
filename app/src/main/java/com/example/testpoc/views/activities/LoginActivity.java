package com.example.testpoc.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.testpoc.databinding.ActivityLoginBinding;
import com.example.testpoc.models.Common;
import com.example.testpoc.utils.enums.LoginCodes;
import com.example.testpoc.viewmodels.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        Common obj = Common.getInstance();

        // For data binding
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        binding.btnLogin.setOnClickListener(view -> {
            viewModel.onLogin();
            binding.progressBar.setVisibility(View.VISIBLE);
        });

        viewModel.errorStatus.observe(this, s -> {
            if (s.equals(LoginCodes.SUCCESS.getValue())) {
                obj.showSnackMessage(getCurrentFocus(), "Login successful", false);
                new Handler().postDelayed(() -> {
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                }, 1300);
            } else if (!s.equals("")) {
                obj.showSnackMessage(getCurrentFocus(), s, false);
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}