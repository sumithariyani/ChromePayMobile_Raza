package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.CustomerTransfers;
import com.example.chromepaymobile.Models.CDBRecycleModel;
import com.example.chromepaymobile.ProfileActivity;
import com.example.chromepaymobile.R;

import java.util.ArrayList;

public class CDBRecycleAdapter extends RecyclerView.Adapter<CDBRecycleAdapter.ViewHolder> {

    ArrayList<CDBRecycleModel> cDBList;
    String id;
    Context context;

    public
    CDBRecycleAdapter(ArrayList<CDBRecycleModel> cDBList, String id, Context context) {
        this.cDBList = cDBList;
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public CDBRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_dashboard_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CDBRecycleAdapter.ViewHolder holder, int position) {

        CDBRecycleModel cdbRecycleModel = cDBList.get(position);

        holder.textView.setText(cdbRecycleModel.getName());
        holder.imageView.setImageResource(cdbRecycleModel.getImg());


        final int pos = holder.getAdapterPosition();

        if (pos == 0){
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ProfileActivity.class);
                    intent.putExtra("cust_id",id);
                    context.startActivity(intent);
                }
            });
        }

        if (pos == 1){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, CustomerTransfers.class);
                    context.startActivity(intent);
                }
            });
        }

        if (pos == 2){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, cdbRecycleModel.getName()+" Coming Soon", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (pos == 3){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, cdbRecycleModel.getName()+" Coming Soon", Toast.LENGTH_SHORT).show();

                }
            });
        }

        if (pos == 4){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, cdbRecycleModel.getName()+" Coming Soon", Toast.LENGTH_SHORT).show();

                }
            });
        }

        if (pos == 5){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, cdbRecycleModel.getName()+" Coming Soon", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cDBList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.profile_tv_cdb);
            imageView = itemView.findViewById(R.id.dash_ic);
            relativeLayout = itemView.findViewById(R.id.parent_l_cdb);
        }
    }
}
