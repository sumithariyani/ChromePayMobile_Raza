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

import com.example.chromepaymobile.LoanPassActivity;
import com.example.chromepaymobile.LoanPassDetailActivty;
import com.example.chromepaymobile.Models.LoanPassModel;
import com.example.chromepaymobile.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoanPassAdapter extends RecyclerView.Adapter<LoanPassAdapter.ViewHolder> {

    ArrayList<LoanPassModel> passList;
    Context context;

    public LoanPassAdapter(ArrayList<LoanPassModel> passList, Context context) {
        this.passList = passList;
        this.context = context;
    }

    @NonNull
    @Override
    public LoanPassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_pass_customer_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanPassAdapter.ViewHolder holder, int position) {

        LoanPassModel loanPassModel = passList.get(position);

        holder.name.setText(loanPassModel.getName());
        holder.number.setText(loanPassModel.getPhone());
        holder.date.setText(loanPassModel.getDate());
        holder.passBtn.setText(loanPassModel.getStatus());

        Picasso.get()
                .load(loanPassModel.getPhoto())
                .placeholder(R.drawable.all_dids_06)
                .fit()
                .into(holder.profileImg);

        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, LoanPassDetailActivty.class);
                intent.putExtra("_ID",loanPassModel.getId());
                intent.putExtra("name", loanPassModel.getName());
                intent.putExtra("number", loanPassModel.getPhone());
                intent.putExtra("status", loanPassModel.getStatus());
                intent.putExtra("photo", loanPassModel.getPhoto());
                intent.putExtra("orgName", loanPassModel.getOrgName());
                intent.putExtra("loanType", loanPassModel.getLoanType());
                intent.putExtra("interest", loanPassModel.getInterest());
                intent.putExtra("EMI", loanPassModel.getEMI());
                intent.putExtra("totalAmount", loanPassModel.getTotalAmount());
                intent.putExtra("durationYear", loanPassModel.getDurationYear());
                intent.putExtra("totalInterestAmount", loanPassModel.getTotalInterest());

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        return passList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, number, date;
        ImageView profileImg, next;
        MaterialButton passBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.lp_name);
            number = itemView.findViewById(R.id.phone_lp);
            date = itemView.findViewById(R.id.lp_date_tv);
            next = itemView.findViewById(R.id.ic_next_lp);
            profileImg = itemView.findViewById(R.id.image_profile_lp);
            passBtn = itemView.findViewById(R.id.pass_button);
        }
    }
}
