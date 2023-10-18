package com.example.testpoc.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.testpoc.R;
import com.example.testpoc.databinding.FragmentBleBinding;
import com.example.testpoc.models.BLE;
import com.example.testpoc.views.adapters.BleListItemAdapter;

import java.util.ArrayList;

public class BleFragment extends Fragment {

    FragmentBleBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBleBinding.inflate(getLayoutInflater());

        ArrayList<String> sampleData = new ArrayList<String>();
        sampleData.add("Device 1");
        sampleData.add("Device 2");
        sampleData.add("Device 3");
        sampleData.add("Device 4");
        sampleData.add("Device 5");
        sampleData.add("Device 6");
        sampleData.add("Device 7");
        sampleData.add("Device 8");
        sampleData.add("Device 9");
        sampleData.add("Device 10");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.bleRecyclerView.setLayoutManager(layoutManager);

        BleListItemAdapter adapter = new BleListItemAdapter(sampleData);
        binding.bleRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        BLE bleObj = new BLE();
        bleObj.initBle(requireActivity().getApplication());

        binding.btnBleScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bleObj.AttemptBleScan();
            }
        });



        return binding.getRoot();
    }
}