package com.example.testpoc.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testpoc.databinding.FragmentNetworkBinding;
import com.example.testpoc.models.Common;
import com.example.testpoc.utils.User;
import com.example.testpoc.viewmodels.HomeActivityViewModel;
import com.example.testpoc.views.adapters.UserListAdapter;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkFragment extends Fragment {

    FragmentNetworkBinding binding;
    HomeActivityViewModel viewModel;
    ArrayList<User> userList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNetworkBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

//        binding.bntMakeApiCall.setOnClickListener(view -> {
//            makeSimpleNetworkCall();
//        });

        UserListAdapter adapter = new UserListAdapter(userList);
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUsers.addItemDecoration(new MaterialDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        binding.recyclerViewUsers.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        viewModel.apiCallStatus.observe(getViewLifecycleOwner(), (status) -> {
            if (status.contains("Fetching")) {
                binding.progressBarApiRequest.setVisibility(View.VISIBLE);
            } else {
                binding.progressBarApiRequest.setVisibility(View.INVISIBLE);
            }
            Common.getInstance().showSnackMessage(binding.getRoot(), status, true);
        });

        viewModel.listOfUsers.observe(getViewLifecycleOwner(), (users) -> {
            userList.clear();
            userList.addAll(users);
            adapter.notifyDataSetChanged();
        });

        binding.bntMakeApiCall.setOnClickListener(view -> {
            new Thread(() -> {
                viewModel.fetchUserDataFromNetwork();
            }).start();
        });

        return binding.getRoot();
    }

    public void makeSimpleNetworkCall() {
        Thread networkCall = new Thread(() -> {
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/users");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";

                Logger.d("Response:\n" + response);

//                new Handler(Looper.getMainLooper()).post(() -> {
//                    binding.tvResponse.setText(String.valueOf(response));
//                });

                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        networkCall.start();
    }

}

