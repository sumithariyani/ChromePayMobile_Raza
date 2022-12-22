package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.LoanRequestActivity;
import com.example.chromepaymobile.Models.TypesLoanModel;
import com.example.chromepaymobile.R;
import com.google.android.material.button.MaterialButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TypeLoanAdapter extends RecyclerView.Adapter<TypeLoanAdapter.ViewHolder> {

    ArrayList<TypesLoanModel> typrList;
    Context context;
    String ordId;
    String custID;

    public TypeLoanAdapter(ArrayList<TypesLoanModel> typrList, Context context, String ordId, String custID) {
        this.typrList = typrList;
        this.context = context;
        this.ordId = ordId;
        this.custID = custID;
    }

    @NonNull
    @Override
    public TypeLoanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_of_loan_recycle_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeLoanAdapter.ViewHolder holder, int position) {

        System.out.println("adapter"+custID);
        TypesLoanModel typesLoanModel = typrList.get(position);

        System.out.println("fghfsdfhgh"+custID);
        holder.name.setText(typesLoanModel.getName());

        holder.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoanRequestActivity.class);
                intent.putExtra("custID",custID);
                intent.putExtra("orgId",ordId);
                intent.putExtra("loan_type",typesLoanModel.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return typrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        MaterialButton applyBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.loan_type_name);
            applyBtn = itemView.findViewById(R.id.apply_btn);
        }
    }
}
