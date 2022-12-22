package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.Models.AgentCommissionModel;
import com.example.chromepaymobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgentCommissionAdapter extends RecyclerView.Adapter<AgentCommissionAdapter.ViewHolder> {

    ArrayList<AgentCommissionModel> agentList;
    Context context;

    public AgentCommissionAdapter(ArrayList<AgentCommissionModel> agentList, Context context) {
        this.agentList = agentList;
        this.context = context;
    }

    @NonNull
    @Override
    public AgentCommissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_comission_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentCommissionAdapter.ViewHolder holder, int position) {

        AgentCommissionModel agentCommissionModel = agentList.get(position);

        Picasso.get()
                        .load(agentCommissionModel.getImg())
                                .placeholder(R.drawable.mask_group)
                                        .fit()
                                                .into(holder.custImg);

        holder.custName.setText(agentCommissionModel.getName());
        holder.amount.setText(agentCommissionModel.getCommission());
        holder.date.setText(agentCommissionModel.getDate());
    }

    @Override
    public int getItemCount() {
        return agentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView custName,date;
        TextView amount;
        CircleImageView custImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            custName = itemView.findViewById(R.id.cust_tv);
            amount = itemView.findViewById(R.id.amount_tv);
            custImg = itemView.findViewById(R.id.cust_img);
            date = itemView.findViewById(R.id.date);
        }
    }
}
