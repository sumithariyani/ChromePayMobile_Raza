package com.example.chromepaymobile.Adapter;

import static android.content.Context.MODE_PRIVATE;

import static androidx.camera.core.CameraX.getContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.example.chromepaymobile.RegisterCustomer3Activity;
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
//                Intent intent = new Intent(context, ProfileActivity.class);
//                intent.putExtra("cust_id",allDidModel.getId());
//                context.startActivity(intent);
                VerifyApi(allDidModel.getPhone());
                Dialog(allDidModel.getPhone(), allDidModel.getId());
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

    public void Dialog(String phone, String id){

                final Dialog dialog = new Dialog(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.cuatomer_view_verify_dialog,null);
                ImageView dis = (ImageView) mView.findViewById(R.id.dis_dialog_loan);
                MaterialButton verify = (MaterialButton) mView.findViewById(R.id.verify_did_btn);
                EditText editText1 = (EditText) mView.findViewById(R.id.editText1);
                EditText editText2 = (EditText) mView.findViewById(R.id.editText2);
                EditText editText3 = (EditText) mView.findViewById(R.id.editText3);
                EditText editText4 = (EditText) mView.findViewById(R.id.editText4);
                EditText editText5 = (EditText) mView.findViewById(R.id.editText5);
                EditText editText6 = (EditText) mView.findViewById(R.id.editText6);

                dialog.setContentView(mView);
        {
            editText1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText2.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText3.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText4.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText5.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText6.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
        {
            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText1.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            editText3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText2.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            editText4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText3.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText4.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText5.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        String otp = editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString()+editText5.getText().toString()+editText6.getText().toString();


                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("OTP",otp);
                        jsonBody.put("phoneNo",phone);

                        System.out.println("jsonBody"+jsonBody);

                        final String mRequestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.CustomerViewVerify, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("responseOtp "+ response);
                                try {

                                    JSONObject jsonObject = new JSONObject(response);

                                    boolean status = jsonObject.getBoolean("status");
                                    String msg = jsonObject.getString("msg");

                                    if (status == true){
                                        Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                                        Intent intent = new Intent(context, ProfileActivity.class);
                                                        intent.putExtra("cust_id",id);
                                                        context.startActivity(intent);
                                      /*  final Dialog dialog1 = new Dialog(RegisterCustomer3Activity.this, android.R.style.Theme_Translucent_NoTitleBar);
                                        View mView = getLayoutInflater().inflate(R.layout.did_success_dialog_layout, null);
*/
/*
                                        MaterialButton ok = (MaterialButton) mView.findViewById(R.id.ok_btn);
*/
/*
                                        dialog1.setContentView(mView);
*/

                                        // Verify OTP Api////////////

//                                        ok.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                Intent intent = new Intent(RegisterCustomer3Activity.this, CustomerDashBoardActivity.class);
//                                                startActivity(intent);
//
//                                            }
//                                        });
//                                        dialog1.show();
                                    }else {
                                        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("otp error " +error);
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

//                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                                5000,
//                                1,
//                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                        ));
                        requestQueue.add(stringRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        dialog.show();


    }

    public void VerifyApi(String phone){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("phoneNo",phone);

            System.out.println("jsonBody"+jsonBody);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.CustomerView, new Response.Listener<String>() {
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
