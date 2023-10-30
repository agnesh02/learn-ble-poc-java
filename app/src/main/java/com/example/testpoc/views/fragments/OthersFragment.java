package com.example.testpoc.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testpoc.R;
import com.example.testpoc.databinding.FragmentOthersBinding;
import com.example.testpoc.models.Common;
import com.example.testpoc.utils.Fruit;
import com.example.testpoc.views.activities.SampleActivity;
import com.example.testpoc.views.adapters.CustomSpinnerAdapter;

import java.util.ArrayList;

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


        ArrayList<Fruit> sampleFruitsList = new ArrayList<>();
        sampleFruitsList.add(new Fruit(R.drawable.apple, "Apple"));
        sampleFruitsList.add(new Fruit(R.drawable.orange, "Orange"));
        sampleFruitsList.add(new Fruit(R.drawable.banana, "Banana"));
        sampleFruitsList.add(new Fruit(R.drawable.grape, "Grapes"));
        sampleFruitsList.add(new Fruit(R.drawable.strawberry, "Strawberry"));


        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(sampleFruitsList);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = sampleFruitsList.get(i).getFruitName();
                binding.tvSpinnerContent.setText("Current selection: " + item);
                Common.getInstance().showSnackMessage(requireView(), item, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Common.getInstance().showSnackMessage(requireView(), "Nothing selected", true);
            }
        });

        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    binding.tvCheckBoxState.setText("Hello.. I am currently checked :)");
                } else {
                    binding.tvCheckBoxState.setText("Hi.. I am currently unchecked :(");
                }
            }
        });

        binding.btnSleep.setOnClickListener(view -> {
            Common.getInstance().showSnackMessage(binding.getRoot(), "Sleep attempt", true);
        });

        return binding.getRoot();
    }
}