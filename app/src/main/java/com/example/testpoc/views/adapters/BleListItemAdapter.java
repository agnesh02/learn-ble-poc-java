package com.example.testpoc.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpoc.R;

import java.util.ArrayList;

public class BleListItemAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<String> list;

    public BleListItemAdapter(ArrayList<String> data){
        this.list = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(list.get(position));
        holder.subtitle.setText("Status : "+position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    TextView title = itemView.findViewById(R.id.textView1);
    TextView subtitle = itemView.findViewById(R.id.textView2);

}
