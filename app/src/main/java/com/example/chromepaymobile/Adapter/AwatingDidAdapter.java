package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.AwatingDIDModel;
import com.example.chromepaymobile.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class AwatingDidAdapter extends RecyclerView.Adapter<AwatingDidAdapter.ViewHolder> {

    ArrayList<AwatingDIDModel> awatingList;
    Context context;


    public AwatingDidAdapter(ArrayList<AwatingDIDModel> awatingList, Context context) {
        this.awatingList = awatingList;
        this.context = context;

    }

    @NonNull
    @Override
    public AwatingDidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.awating_did_layopout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AwatingDidAdapter.ViewHolder holder, int position) {

        AwatingDIDModel awatingDIDModel = awatingList.get(position);

        holder.name.setText(awatingDIDModel.getName());
        holder.phone.setText(String.valueOf(awatingDIDModel.getPhone()));
        holder.awatingDid.setText(awatingDIDModel.getDid());

        Picasso.get()
                .load(awatingDIDModel.getPhoto())
                .placeholder(R.drawable.all_dids_06)
                .fit()
                .into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return awatingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,phone, awatingDid;
        ImageView profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.awating_did_name);
            phone = itemView.findViewById(R.id.phone_awating_did);
            profile = itemView.findViewById(R.id.profile_img_awating_did);
            awatingDid = itemView.findViewById(R.id.awating_did);
        }

    }

    public void VerifyApi(String id){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.VerifyCustomer+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VERIFY_CUSTOMER_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");
                        String msg = jsonObject.getString("msg");

                        if (status == true){

                            Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(stringRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
