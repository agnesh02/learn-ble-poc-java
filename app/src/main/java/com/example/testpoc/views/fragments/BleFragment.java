package com.example.testpoc.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpoc.databinding.FragmentBleBinding;
import com.example.testpoc.models.Common;
import com.example.testpoc.utils.Device;
import com.example.testpoc.viewmodels.HomeActivityViewModel;
import com.example.testpoc.views.adapters.BleListItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class BleFragment extends Fragment {

    FragmentBleBinding binding;
    HomeActivityViewModel viewModel;
    List<Device> discoveredDevices;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBleBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(requireActivity()).get(HomeActivityViewModel.class);

        // Setting up custom recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        binding.bleRecyclerView.addItemDecoration(itemDecoration);
        binding.bleRecyclerView.setLayoutManager(layoutManager);

        discoveredDevices = viewModel.listOfDiscoveredDevices.getValue();
        BleListItemAdapter adapter = new BleListItemAdapter(discoveredDevices, viewModel);

        binding.bleRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        viewModel.initializeBle();

        viewModel.bleFeedbackMessage.observe(getViewLifecycleOwner(), message -> {
            if (!message.equals("")) {
                Common.getInstance().showSnackMessage(getView(), message, true);
            }
        });

        viewModel.isScanningForDevices.observe(getViewLifecycleOwner(), isScanning -> {
            if (isScanning) {
                binding.btnBleScan.setText("Scanning...");
                viewModel.listOfDiscoveredDevices.postValue(new ArrayList<>());
                binding.progressBarScan.setVisibility(View.VISIBLE);
                binding.btnBleScan.setEnabled(false);
            } else {
                binding.btnBleScan.setText("Start scan");
                binding.progressBarScan.setVisibility(View.INVISIBLE);
                binding.btnBleScan.setEnabled(true);
            }
        });

        binding.btnBleScan.setOnClickListener(view -> viewModel.requestPermissions(requireActivity()));

        viewModel.listOfDiscoveredDevices.observe(getViewLifecycleOwner(), listOfBleDevices -> {
            discoveredDevices.clear();
            discoveredDevices.addAll(listOfBleDevices);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }
}