package com.example.testpoc.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testpoc.databinding.ActivityLoginBinding;
import com.example.testpoc.models.Common;
import com.example.testpoc.models.LoginCodes;
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

        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.onLogin();
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        });

        viewModel.errorStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == LoginCodes.SUCCESS.getValue()) {
                    obj.showMessage(getApplicationContext(), "Login successful");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                } else if(!s.equals("")) {
                    obj.showMessage(getApplicationContext(), s);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}