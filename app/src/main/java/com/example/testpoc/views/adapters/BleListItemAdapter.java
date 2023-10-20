package com.example.testpoc.views.adapters;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testpoc.R;
import com.example.testpoc.models.Common;
import com.example.testpoc.utils.enums.BleConnectionStatus;
import com.example.testpoc.utils.Device;
import com.example.testpoc.viewmodels.HomeActivityViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class BleListItemAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final List<Device> list;
    private final HomeActivityViewModel viewModel;

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
        device.getDeviceName().observeForever(deviceName -> holder.title.setText(deviceName));

        holder.subtitle.setText("Address : " + device.getMacAddress());
        device.getIsConnected().observeForever(bleConnectionStatus -> {
            if (bleConnectionStatus.getConnectionStatus() == BleConnectionStatus.DISCONNECTED.getConnectionStatus()) {
                holder.connectivityButton.setText(BleConnectionStatus.DISCONNECTED.getActionMessage());
                holder.readButton.setVisibility(View.INVISIBLE);
                holder.writeButton.setVisibility(View.INVISIBLE);
            } else if (bleConnectionStatus.getConnectionStatus() == BleConnectionStatus.CONNECTED.getConnectionStatus()) {
                holder.connectivityButton.setText(BleConnectionStatus.CONNECTED.getActionMessage());
                holder.readButton.setVisibility(View.VISIBLE);
                holder.writeButton.setVisibility(View.VISIBLE);
            } else if (bleConnectionStatus.getConnectionStatus() == BleConnectionStatus.CONNECTING.getConnectionStatus()) {
                holder.connectivityButton.setText(BleConnectionStatus.CONNECTING.getActionMessage());
                holder.connectivityButton.setEnabled(false);
            }
        });

        holder.connectivityButton.setOnClickListener(view -> {
            String connectionStatus = String.valueOf(holder.connectivityButton.getText());
            if (connectionStatus.equals(BleConnectionStatus.CONNECTED.getActionMessage())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Disconnect BLE Device");
                builder.setMessage("Are you sure you want to disconnect " + device.getDeviceName().getValue() + " ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Disconnect", (dialog, which) -> viewModel.disconnectDevice(device));
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else {
                viewModel.connectDevice(device);
            }
        });

        holder.readButton.setOnClickListener(view -> viewModel.readData(device.getBleDevice()));

        holder.writeButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Enter new display name for " + device.getDeviceName().getValue());
            final TextInputLayout textInputParent = new TextInputLayout(view.getContext(), null, com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
            final TextInputEditText textInputChild = new TextInputEditText(view.getContext());
            textInputParent.setPadding(60, 30, 60, 30);
            textInputParent.setHint("New device name");
            textInputParent.addView(textInputChild);
            textInputChild.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(textInputParent);
            builder.setPositiveButton("OK", (dialog, which) -> {
                String newDeviceName = textInputChild.getText().toString().trim();
                Common.getInstance().showSnackMessage(view, "Changing device name of " + device.getDeviceName() + " to " + newDeviceName, true);
                viewModel.writeData(device, newDeviceName);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
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
    Button writeButton = itemView.findViewById(R.id.btn_write);


}
