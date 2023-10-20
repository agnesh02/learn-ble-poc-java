package com.example.testpoc.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testpoc.databinding.FragmentOthersBinding;
import com.example.testpoc.views.activities.SampleActivity;

public class OthersFragment extends Fragment {

    FragmentOthersBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOthersBinding.inflate(getLayoutInflater());

        // Navigating to activity with the data entered
        binding.btnNavToSample.setOnClickListener(view -> {
            String dataToBePassed = String.valueOf(binding.etPassData.getText());
            Intent i = new Intent(getContext(), SampleActivity.class);
            i.putExtra("DATA", dataToBePassed);
            startActivity(i);
        });

        return binding.getRoot();
    }
}