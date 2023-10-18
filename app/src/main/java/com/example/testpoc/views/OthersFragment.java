package com.example.testpoc.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.testpoc.databinding.FragmentOthersBinding;

public class OthersFragment extends Fragment {

    FragmentOthersBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOthersBinding.inflate(getLayoutInflater());

        binding.btnNavToSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataToBePassed = String.valueOf(binding.etPassData.getText());
                Intent i = new Intent(getContext(), SampleActivity.class);
                i.putExtra("DATA", dataToBePassed);
                startActivity(i);
            }
        });

        return binding.getRoot();
    }
}