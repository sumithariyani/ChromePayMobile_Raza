package com.example.chromepaymobile.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.chromepaymobile.AgentWalletActivity;
import com.example.chromepaymobile.AllDidActivity;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.FaceRecognition;
import com.example.chromepaymobile.LoanApplyFormActivity;
import com.example.chromepaymobile.Models.AllDidModel;
import com.example.chromepaymobile.ProfileActivity;
import com.example.chromepaymobile.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AllDidAdapter extends RecyclerView.Adapter<AllDidAdapter.ViewHolder> {

    String verified;
    ArrayList<AllDidModel> allDidList;
    Context context;

    public AllDidAdapter(ArrayList<AllDidModel> allDidList, Context context) {
        this.allDidList = allDidList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllDidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_did_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllDidAdapter.ViewHolder holder, int position) {

        AllDidModel allDidModel = allDidList.get(position);

        holder.name.setText(allDidModel.getName());
        holder.phone.setText(allDidModel.getPhone());
        holder.did.setText(allDidModel.getDID());
        Picasso.get()
                .load(allDidModel.getImg())
                .placeholder(R.drawable.all_dids_06)
                .fit()
                .into(holder.profile);

        System.out.println(allDidModel.getStatus());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("cust_id",allDidModel.getId());
                context.startActivity(intent);
            }
        });

      /*  if (allDidModel.getStatus().equals("verified")){

            Drawable placeholder = holder.verifyImg.getContext().getDrawable(R.drawable.all_dids_11);
            holder.verifyImg.setImageDrawable(placeholder);

        }else {

            Drawable placeholder = holder.verifyImg.getContext().getDrawable(R.drawable.all_dids_08);
            holder.verifyImg.setImageDrawable(placeholder);
        }

        holder.verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyApi(allDidModel.getId());
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeleteApi(allDidModel.getId());
            }
        });

        holder.addLoanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoanApplyFormActivity.class);
                intent.putExtra("cust_id",allDidModel.getId());
                intent.putExtra("name",allDidModel.getName());
                intent.putExtra("mobile",allDidModel.getPhone());
                intent.putExtra("email",allDidModel.getEmail());
                intent.putExtra("photo",allDidModel.getPhone());
                context.startActivity(intent);
            }
        });

        holder.face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, FaceRecognition.class);
                intent.putExtra("cust_id",allDidModel.getId());
                context.startActivity(intent);
            }
        });

        holder.agentWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AgentWalletActivity.class);
                intent.putExtra("cust_id",allDidModel.getId());
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return allDidList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, phone, did;
        ImageView profile,verifyImg;
        LinearLayout linearLayout,verify,face,addLoanBtn,agentWallet;
        RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.all_did_name);
            phone = itemView.findViewById(R.id.phone_all_did);
            did = itemView.findViewById(R.id.all_did_tv);
            profile = itemView.findViewById(R.id.image_profile_all_did);
            linearLayout = itemView.findViewById(R.id.del_btn);
            verify = itemView.findViewById(R.id.verify_l);
            parent = itemView.findViewById(R.id.parent_l_agent);
          /*  verifyImg = itemView.findViewById(R.id.image_check_uncheck);
            face = itemView.findViewById(R.id.face_reco);
            addLoanBtn = itemView.findViewById(R.id.add_loans);
            agentWallet = itemView.findViewById(R.id.agent_wallet);*/
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

    public void DeleteApi(String id){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("userID",id);

            System.out.println("userID"+id);
            final String mRequestBody = jsonBody.toString();

            SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String token = sharedPreferences.getString("token","");

            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, AllUrl.DeleteCustomer+token+"/"+ id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("DELETE_CUSTOMER_VOLLEY", response);

                    try{
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
