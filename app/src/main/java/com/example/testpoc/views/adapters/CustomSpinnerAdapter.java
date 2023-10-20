package com.example.testpoc.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testpoc.R;
import com.example.testpoc.utils.Fruit;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter {

    ArrayList<Fruit> fruitsList;

    public CustomSpinnerAdapter(ArrayList<Fruit> fruitsList) {
        this.fruitsList = fruitsList;
    }

    @Override
    public int getCount() {
        return fruitsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_spinner, viewGroup, false);
        ImageView icon = view.findViewById(R.id.fruit_image);
        TextView name = view.findViewById(R.id.tv_fruit_name);

        Fruit fruitItem = fruitsList.get(i);

        icon.setImageResource(fruitItem.getFruitImage());
        name.setText(fruitItem.getFruitName());
        return view;
    }
}
