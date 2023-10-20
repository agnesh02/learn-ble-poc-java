package com.example.testpoc.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testpoc.R;
import com.example.testpoc.databinding.ActivityHomeBinding;
import com.example.testpoc.views.fragments.BleFragment;
import com.example.testpoc.views.fragments.CounterFragment;
import com.example.testpoc.views.fragments.OthersFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initializing a screen
        changeScreen(new BleFragment());
        binding.bottomNavigation.setSelectedItemId(R.id.menu_ble);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
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
        });

    }

    /**
     * Method to switch screens / replace fragments
     *
     * @param fragment {@link Fragment} The desired fragment which is to be accessed.
     */
    private void changeScreen(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.homeActivityFrame.getId(), fragment);
        transaction.commit();
    }
}