package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.Models.MSRecycleModel;
import com.example.chromepaymobile.R;

import java.util.ArrayList;

public class MSRecycleAdapter extends RecyclerView.Adapter<MSRecycleAdapter.ViewHolder> {

    ArrayList<MSRecycleModel> msList;
    Context context;

    public MSRecycleAdapter(ArrayList<MSRecycleModel> msList, Context context) {
        this.msList = msList;
        this.context = context;
    }

    @NonNull
    @Override
    public MSRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_service_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MSRecycleAdapter.ViewHolder holder, int position) {

        MSRecycleModel msRecycleModel = msList.get(position);

        holder.textView.setText(msRecycleModel.getName());
        holder.imageView.setImageResource(msRecycleModel.getImg());

        final int pos = holder.getAdapterPosition();

        if (pos == 0){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, msRecycleModel.getName()+" Coming Soon", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (pos == 1){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, msRecycleModel.getName()+" Coming Soon", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (pos == 2){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, msRecycleModel.getName()+" Coming Soon", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (pos == 3){

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, msRecycleModel.getName()+" Coming Soon", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return msList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.mic_ic);
            textView = itemView.findViewById(R.id.mic_service_tv);
            relativeLayout = itemView.findViewById(R.id.card_ms1);
        }
    }
}
