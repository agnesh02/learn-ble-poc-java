package com.example.testpoc.views.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testpoc.R;
import com.example.testpoc.databinding.FragmentHeartRateBinding;
import com.example.testpoc.models.Common;
import com.example.testpoc.viewmodels.HomeActivityViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HeartRateFragment extends Fragment {

    FragmentHeartRateBinding binding;
    HomeActivityViewModel viewModel;
    final static String HEART_RATE_SENSOR_MAC = "F8:7E:39:61:16:31";
    SQLiteDatabase database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHeartRateBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

        database = viewModel.initializeDatabase();

        viewModel.heartRateSensorConnectivity.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    binding.btnStartMonitoringHeartRate.setVisibility(View.VISIBLE);
                    binding.btnConnectWithHeartRateSensor.setVisibility(View.INVISIBLE);
                    binding.btnDisconnectWithHeartRateSensor.setVisibility(View.VISIBLE);
                } else {
                    binding.btnStartMonitoringHeartRate.setVisibility(View.INVISIBLE);
                    binding.btnStopMonitoringHeartRate.setVisibility(View.INVISIBLE);
                    binding.btnConnectWithHeartRateSensor.setVisibility(View.VISIBLE);
                    binding.btnDisconnectWithHeartRateSensor.setVisibility(View.INVISIBLE);
                }
            }
        });

        viewModel.heartRate.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String reading) {
                String heartRate = String.format("%s BPM", reading);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String formattedDate = formatter.format(date);
                binding.tvStatusHeartRateSensor.setText(heartRate);
                viewModel.insertHeartRateRecord(database, formattedDate, heartRate);
            }
        });

        viewModel.heartRateDeviceConnectivityMessages.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Common.getInstance().showSnackMessage(binding.getRoot(), s, true);
            }
        });


        binding.btnConnectWithHeartRateSensor.setOnClickListener(view -> {
            viewModel.connectHeartRateSensor(HEART_RATE_SENSOR_MAC);
        });

        binding.btnStartMonitoringHeartRate.setOnClickListener(view -> {
            viewModel.startMonitoringHeartRate();
            binding.btnStartMonitoringHeartRate.setVisibility(View.INVISIBLE);
            binding.btnStopMonitoringHeartRate.setVisibility(View.VISIBLE);
        });

        binding.btnStopMonitoringHeartRate.setOnClickListener(view -> {
            viewModel.stopMonitoringHeartRate();
            binding.btnStartMonitoringHeartRate.setVisibility(View.VISIBLE);
            binding.btnStopMonitoringHeartRate.setVisibility(View.INVISIBLE);
        });

        binding.btnDisconnectWithHeartRateSensor.setOnClickListener(view -> {
            viewModel.disconnectHeartRateSensor();
        });

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle("Heart Rate Logs")
                .setMessage("Please find the stored logs below")
                .setNegativeButton("Close", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }));

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogView = layoutInflater.inflate(R.layout.custom_alert, null);

        ListView listView = dialogView.findViewById(R.id.list_view_heart_rate_logs);
        listView.setAdapter(arrayAdapter);

        dialog.setView(dialogView);

        binding.tvShowHeartRateLogs.setOnClickListener(view -> {
            arrayList.clear();
            arrayList.addAll(viewModel.getHeartRateRecords(database));
            arrayAdapter.notifyDataSetChanged();
            dialog.create().show();
        });

        return binding.getRoot();
    }


}
