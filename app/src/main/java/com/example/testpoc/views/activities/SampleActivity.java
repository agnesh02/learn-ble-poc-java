package com.example.testpoc.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testpoc.databinding.ActivitySampleBinding;

public class SampleActivity extends AppCompatActivity {

    ActivitySampleBinding binding;
    String fetchedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Fetching the passed data from predecessor
        Bundle extraItems = getIntent().getExtras();
        if (extraItems != null) {
            fetchedData = extraItems.getString("DATA");
        }

        binding.tvReceivedData.setText(fetchedData);
    }
}