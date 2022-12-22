package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.Models.CustomerModel;
import com.example.chromepaymobile.R;

import java.util.ArrayList;

public class CustomerTransferAdapter extends RecyclerView.Adapter<CustomerTransferAdapter.ViewHolder> {

    ArrayList<CustomerModel> list;
    Context context;

    public CustomerTransferAdapter(ArrayList<CustomerModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerTransferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_transfer_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerTransferAdapter.ViewHolder holder, int position) {

        CustomerModel customerModel = list.get(position);

        holder.name.setText(customerModel.getName());
        holder.amount.setText(customerModel.getAmount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.send_tv);
            amount = itemView.findViewById(R.id.send_amount_tv);
        }
    }
}
