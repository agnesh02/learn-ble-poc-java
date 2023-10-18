package com.example.testpoc.views;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testpoc.R;
import com.example.testpoc.databinding.ActivityHomeBinding;
import com.example.testpoc.viewmodels.HomeActivityViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    HomeActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setSelectedItemId(R.id.menu_ble);

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_ble) {
                    changeScreen(new BleFragment());
                    return true;
                } else if (item.getItemId() == R.id.menu_counter) {
                    changeScreen(new CounterFragment());
                    return true;
                } else if (item.getItemId() == R.id.menu_others) {
                    changeScreen(new OthersFragment());
                    return true;
                }
                return true;
            }
        });

    }

    private void changeScreen(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.homeActivityFrame.getId(), fragment);
        transaction.commit();
    }
}