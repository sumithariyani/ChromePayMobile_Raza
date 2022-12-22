package com.example.chromepaymobile.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.UnBlockedDidModel;
import com.example.chromepaymobile.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class UnBlockedDidAdpter extends RecyclerView.Adapter<UnBlockedDidAdpter.ViewHolder> {

    ArrayList<UnBlockedDidModel> blockList;
    Context context;

    public UnBlockedDidAdpter(ArrayList<UnBlockedDidModel> blockList, Context context) {
        this.blockList = blockList;
        this.context = context;
    }

    @NonNull
    @Override
    public UnBlockedDidAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.un_blocked_did_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnBlockedDidAdpter.ViewHolder holder, int position) {

        UnBlockedDidModel blockedDidModel = blockList.get(position);

        holder.name.setText(blockedDidModel.getName());
        holder.phone.setText(String.valueOf(blockedDidModel.getPhone()));

        Picasso.get()
                .load(blockedDidModel.getPhoto())
                .placeholder(R.drawable.all_dids_06)
                .fit()
                .into(holder.profile);

        holder.unBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UnBlockedApi(blockedDidModel.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return blockList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,phone;
        ImageView profile;
        LinearLayout unBlock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.blocked_did_name);
            phone = itemView.findViewById(R.id.phone_blocked_did);
            profile = itemView.findViewById(R.id.profile_img_bolcked_did);
            unBlock = itemView.findViewById(R.id.blocked_Btn);
        }
    }

    public void UnBlockedApi(String id){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String agentID = sharedPreferences.getString("ID","");

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("userID",id);

            System.out.println("userID"+id);
            final String mRequestBody = jsonBody.toString();



            StringRequest stringRequest = new StringRequest(Request.Method.PUT, AllUrl.UnBlockedCustomer+agentID+"/"+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("UNBLOCK_CUSTOMER_VOLLEY", response);

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
                    })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    return super.parseNetworkResponse(response);
                }
            };

            requestQueue.add(stringRequest);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
