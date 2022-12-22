package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.LoanApplyCustomerDetaillActivity;
import com.example.chromepaymobile.Models.LoanApplyModel;
import com.example.chromepaymobile.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoanApplyAdapter extends RecyclerView.Adapter<LoanApplyAdapter.ViewHolder> {

    ArrayList<LoanApplyModel> pendingList;
    Context context;

    public LoanApplyAdapter(ArrayList<LoanApplyModel> pendingList, Context context) {
        this.pendingList = pendingList;
        this.context = context;
    }

    @NonNull
    @Override
    public LoanApplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_apply_customer_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanApplyAdapter.ViewHolder holder, int position) {

        LoanApplyModel loanApplyModel = pendingList.get(position);

        holder.name.setText(loanApplyModel.getName());
        holder.statusBtn.setText(loanApplyModel.getStatus());
        holder.number.setText(loanApplyModel.getNumber());
        holder.date.setText(loanApplyModel.getDate());

        Picasso.get()
                .load(loanApplyModel.getPhoto())
                .placeholder(R.drawable.all_dids_06)
                .fit()
                .into(holder.profilePhoto);

        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoanApplyCustomerDetaillActivity.class);
                intent.putExtra("name",loanApplyModel.getName());
                intent.putExtra("phone",loanApplyModel.getNumber());
                intent.putExtra("status",loanApplyModel.getStatus());
                intent.putExtra("photo",loanApplyModel.getPhoto());
                intent.putExtra("oraganistaionName",loanApplyModel.getOrganisationName());
                intent.putExtra("loanType",loanApplyModel.getLoanType());
                intent.putExtra("interest",loanApplyModel.getInterest());
                intent.putExtra("EMI",loanApplyModel.getEMI());
                intent.putExtra("totalAmount",loanApplyModel.getTotalAmount());
                intent.putExtra("durationYear",loanApplyModel.getDurationYear());
                intent.putExtra("totalInterestAmount",loanApplyModel.getTotalInterestAmount());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,number,date;
        ImageView profilePhoto,next;
        MaterialButton statusBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.lac_name);
            number = itemView.findViewById(R.id.phone_lac);
            date = itemView.findViewById(R.id.date_tv);
            statusBtn = itemView.findViewById(R.id.pending_button);
            profilePhoto = itemView.findViewById(R.id.image_profile_lac);
            next = itemView.findViewById(R.id.ic_next);
        }
    }
}
