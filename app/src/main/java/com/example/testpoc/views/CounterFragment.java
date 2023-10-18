package com.example.testpoc.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testpoc.R;
import com.example.testpoc.databinding.FragmentCounterBinding;
import com.example.testpoc.viewmodels.HomeActivityViewModel;

public class CounterFragment extends Fragment {

    FragmentCounterBinding binding;
    HomeActivityViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCounterBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        binding.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.incrementCounter();
            }
        });

        binding.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.decrementCounter();
            }
        });

        return binding.getRoot();
    }
}