package com.example.testpoc.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpoc.R;
import com.example.testpoc.utils.BleConnectionStatus;
import com.example.testpoc.utils.Device;
import com.example.testpoc.viewmodels.HomeActivityViewModel;

import java.util.List;

public class BleListItemAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Device> list;
    private HomeActivityViewModel viewModel;

    public BleListItemAdapter(List<Device> data, HomeActivityViewModel viewModel) {
        this.list = data;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Device device = list.get(position);
        holder.title.setText(device.getDeviceName());
        holder.subtitle.setText("Address : " + device.getMacAddress());
        device.getIsConnected().observeForever(new Observer<BleConnectionStatus>() {
            @Override
            public void onChanged(BleConnectionStatus bleConnectionStatus) {
                if (bleConnectionStatus.getConnectionStatus() == BleConnectionStatus.DISCONNECTED.getConnectionStatus()) {
                    holder.connectivityButton.setText(BleConnectionStatus.DISCONNECTED.getActionMessage());
                    holder.readButton.setVisibility(View.INVISIBLE);
                } else if (bleConnectionStatus.getConnectionStatus() == BleConnectionStatus.CONNECTED.getConnectionStatus()) {
                    holder.connectivityButton.setText(BleConnectionStatus.CONNECTED.getActionMessage());
                    holder.readButton.setVisibility(View.VISIBLE);
                } else if (bleConnectionStatus.getConnectionStatus() == BleConnectionStatus.CONNECTING.getConnectionStatus()) {
                    holder.connectivityButton.setText(BleConnectionStatus.CONNECTING.getActionMessage());
                    holder.connectivityButton.setEnabled(false);
                }
            }
        });

        holder.connectivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String connectionStatus = String.valueOf(holder.connectivityButton.getText());
                if (connectionStatus.equals(BleConnectionStatus.CONNECTED.getActionMessage())) {
                    viewModel.disconnectDevice(device);
                } else {
                    viewModel.connectDevice(device);
                }
            }
        });

        holder.readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.readData(device.getBleDevice());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    TextView title = itemView.findViewById(R.id.tv_device_name);
    TextView subtitle = itemView.findViewById(R.id.tv_device_mac);
    Button connectivityButton = itemView.findViewById(R.id.btn_connect);
    Button readButton = itemView.findViewById(R.id.btn_read);

}
