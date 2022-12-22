package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chromepaymobile.Models.OrgLinkedModel;
import com.example.chromepaymobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrgLinkedAdapter extends RecyclerView.Adapter<OrgLinkedAdapter.ViewHolder> {

    ArrayList<OrgLinkedModel> linkedList;
    Context context;

    public OrgLinkedAdapter(ArrayList<OrgLinkedModel> linkedList, Context context) {
        this.linkedList = linkedList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrgLinkedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.org_linked_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrgLinkedAdapter.ViewHolder holder, int position) {

        OrgLinkedModel orgLinkedModel = linkedList.get(position);

        holder.name.setText(orgLinkedModel.getName());

        Picasso.get()
                .load(orgLinkedModel.getLogo())
                .placeholder(R.drawable.image_2)
                .fit()
                .into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return linkedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView logo;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.fuse_img);
            name = itemView.findViewById(R.id.fuse_name);

        }
    }

}
