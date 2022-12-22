package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.Models.WalletTransactionModel;
import com.example.chromepaymobile.R;

import java.util.ArrayList;

public class WalletTransactionAdapter extends RecyclerView.Adapter<WalletTransactionAdapter.ViewHolder> {


    ArrayList <WalletTransactionModel> transactionList;
    Context context;

    public WalletTransactionAdapter(ArrayList<WalletTransactionModel> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @NonNull
    @Override
    public WalletTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_transaction_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletTransactionAdapter.ViewHolder holder, int position) {

        WalletTransactionModel walletTransactionModel = transactionList.get(position);

        holder.name.setText(walletTransactionModel.getRecieverName());
        holder.amount.setText(walletTransactionModel.getReceivingAmount());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_txt);
            amount = itemView.findViewById(R.id.amount_txt);
        }
    }
}
