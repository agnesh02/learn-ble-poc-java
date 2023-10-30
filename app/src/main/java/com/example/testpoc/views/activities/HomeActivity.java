package com.example.testpoc.views.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testpoc.R;
import com.example.testpoc.databinding.ActivityHomeBinding;
import com.example.testpoc.models.Common;
import com.example.testpoc.views.fragments.BleFragment;
import com.example.testpoc.views.fragments.CounterFragment;
import com.example.testpoc.views.fragments.HeartRateFragment;
import com.example.testpoc.views.fragments.NetworkFragment;
import com.example.testpoc.views.fragments.OthersFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        String loginMsg;

        if (bundle != null) {
            loginMsg = bundle.getString("loginMessage");
        } else {
            loginMsg = "Some error occurred.";
        }

        Common.getInstance().showSnackMessage(binding.getRoot(), loginMsg, false);

        // Initializing a screen
        getSupportActionBar().setTitle("BLE Actions");
        changeScreen(new BleFragment());
        binding.bottomNavigation.setSelectedItemId(R.id.menu_ble);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_ble) {
                getSupportActionBar().setTitle("BLE Actions");
                changeScreen(new BleFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_heart_rate) {
                getSupportActionBar().setTitle("Monitor Heart Rate");
                changeScreen(new HeartRateFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_counter) {
                getSupportActionBar().setTitle("Counter Screen");
                changeScreen(new CounterFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_others) {
                getSupportActionBar().setTitle("Other Controls");
                changeScreen(new OthersFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_network) {
                getSupportActionBar().setTitle("Network");
                changeScreen(new NetworkFragment());
                return true;
            }
            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.android)
                    .setTitle("Logout")
                    .setMessage("Are you sure that you want to logout from this app ?")
                    .setPositiveButton("Logout", ((dialogInterface, i) -> {
                        Common.getInstance().showSnackMessage(binding.getRoot(), "Logging out..", true);
                        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.testpoc", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        startActivity(new Intent(this, LoginActivity.class).putExtra("loginMessage", "Please login."));
                        finish();
                    }))
                    .setNegativeButton("Cancel", ((dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }));
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
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
