package com.example.testpoc.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testpoc.databinding.FragmentCounterBinding;
import com.example.testpoc.viewmodels.HomeActivityViewModel;

public class CounterFragment extends Fragment {

    FragmentCounterBinding binding;
    HomeActivityViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCounterBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

        // For data binding
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        binding.btnIncrement.setOnClickListener(view -> viewModel.incrementCounter());

        binding.btnDecrement.setOnClickListener(view -> viewModel.decrementCounter());

        return binding.getRoot();
    }
}