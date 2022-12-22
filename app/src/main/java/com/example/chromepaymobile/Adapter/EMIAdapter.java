package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.Models.EMIModel;
import com.example.chromepaymobile.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class EMIAdapter extends RecyclerView.Adapter<EMIAdapter.ViewHolder> {

    ArrayList<EMIModel> emiList;
    Context context;

    public EMIAdapter(ArrayList<EMIModel> emiList, Context context) {
        this.emiList = emiList;
        this.context = context;
    }

    @NonNull
    @Override
    public EMIAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emi_detail_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EMIAdapter.ViewHolder holder, int position) {

        EMIModel emiModel = emiList.get(position);

        holder.installNum.setText(emiModel.getInstallmentNo());
        holder.installDate.setText(emiModel.getInstallmentDate());
        holder.payDate.setText(emiModel.getPayDate());
        holder.installAmount.setText(emiModel.getInstallmentPayAmount());
        holder.materialButton.setText(emiModel.getStatus());

        if (emiModel.getStatus().equals("Due")){

            Drawable placeholder = holder.materialButton.getContext().getDrawable(R.drawable.pending_button_background);
            holder.materialButton.setBackground(placeholder);

            holder.materialButton.setTextColor(Color.WHITE);
        }else {
            Drawable placeholder = holder.materialButton.getContext().getDrawable(R.drawable.pass_button_background);
            holder.materialButton.setBackground(placeholder);
            holder.materialButton.setTextColor(Color.parseColor("#42aef8"));

        }
    }

    @Override
    public int getItemCount() {
        return emiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView installNum,installDate, payDate, installAmount;
        MaterialButton materialButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            installNum = itemView.findViewById(R.id.install_no);
            installDate = itemView.findViewById(R.id.install_date);
            payDate = itemView.findViewById(R.id.pay_date);
            installAmount = itemView.findViewById(R.id.install_amount);
            materialButton = itemView.findViewById(R.id.status_button);

        }
    }
}
