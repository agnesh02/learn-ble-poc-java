package com.example.testpoc.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpoc.R;
import com.example.testpoc.utils.User;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<MyUserViewHolder> {

    ArrayList<User> userList = new ArrayList<>();

    public UserListAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_user_list, parent, false);
        return new MyUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.username.setText(user.getName());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

class MyUserViewHolder extends RecyclerView.ViewHolder {

    public MyUserViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    TextView username = itemView.findViewById(R.id.tv_user_name);
    TextView email = itemView.findViewById(R.id.tv_user_email);
}


