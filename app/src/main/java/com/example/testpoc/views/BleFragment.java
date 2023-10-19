package com.example.testpoc.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBleBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(requireActivity()).get(HomeActivityViewModel.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        binding.bleRecyclerView.addItemDecoration(itemDecoration);
        binding.bleRecyclerView.setLayoutManager(layoutManager);

        discoveredDevices = viewModel.listOfDiscoveredDevices.getValue();
        BleListItemAdapter adapter = new BleListItemAdapter(discoveredDevices, viewModel);

        binding.bleRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        viewModel.initializeBle();

        viewModel.bleErrorMessage.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (!message.equals("")) {
                    Common.getInstance().showMessage(requireActivity(), message);
                }
            }
        });

        viewModel.isScanningForDevices.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isScanning) {
                if (isScanning) {
                    binding.btnBleScan.setText("Scanning...");
                    viewModel.listOfDiscoveredDevices.postValue(new ArrayList());
                    binding.progressBarScan.setVisibility(View.VISIBLE);
                    binding.btnBleScan.setEnabled(false);
                } else {
                    binding.btnBleScan.setText("Start scan");
                    binding.progressBarScan.setVisibility(View.INVISIBLE);
                    binding.btnBleScan.setEnabled(true);
                }
            }
        });

        binding.btnBleScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.requestPermissions(requireActivity());
            }
        });

        viewModel.listOfDiscoveredDevices.observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> listOfBleDevices) {
                discoveredDevices.clear();
                discoveredDevices.addAll(listOfBleDevices);
                adapter.notifyDataSetChanged();
            }
        });


        return binding.getRoot();
    }
}