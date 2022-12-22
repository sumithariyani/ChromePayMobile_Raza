package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Adapter.WalletTransactionAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.WalletTransactionModel;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AgentWalletActivity extends AppCompatActivity {

    TextView currentAmount;
    TextView walletAddress;
    LinearLayout payButton;
    RecyclerView transactionRecycle;

    ArrayList<WalletTransactionModel> transactionList = new ArrayList<>();
    WalletTransactionAdapter walletTransactionAdapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_wallet);

        currentAmount = findViewById(R.id.current_amount_tv);
        walletAddress = findViewById(R.id.wallet_address_txt);
        payButton = findViewById(R.id.pay_button);
        transactionRecycle = findViewById(R.id.transaction_recycle);


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog();
            }
        });

        GetWallet();
        Transaction();

    }

    public void GetWallet() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.CustomerWalllet + getIntent().getStringExtra("cust_id"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("WALLET_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true) {

                            JSONObject object = jsonObject.getJSONObject("find_cust_wallet");

                            String current_Amount = object.getString("current_Amount");
                            String wallet_Address = object.getString("wallet_Address");


                            currentAmount.setText("$"+ current_Amount);
                            walletAddress.setText("#####"+wallet_Address.substring(42,46));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AgentWalletActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Transaction(){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.LatestTransactions + getIntent().getStringExtra("cust_id"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("TRANS_DETAIL_VOLLEY", response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            JSONArray jsonArray = jsonObject.getJSONArray("sort");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("_id");
                                String recieverName = object.getString("recieverName");
                                String receivingAmount = object.getString("receivingAmount");

                                WalletTransactionModel walletTransactionModel = new WalletTransactionModel();

                                walletTransactionModel.set_id(id);
                                walletTransactionModel.setRecieverName(recieverName);
                                walletTransactionModel.setReceivingAmount(receivingAmount);

                                transactionList.add(walletTransactionModel);

                                walletTransactionAdapter = new WalletTransactionAdapter(transactionList, AgentWalletActivity.this);
                                transactionRecycle.setAdapter(walletTransactionAdapter);
                                walletTransactionAdapter.notifyDataSetChanged();
                            }

                        }else {
                            Toast.makeText(AgentWalletActivity.this, "status false", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AgentWalletActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Dialog(){

        final Dialog dialog = new Dialog(AgentWalletActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        View mView = getLayoutInflater().inflate(R.layout.pay_amount_dialog, null);

        EditText number = (EditText) mView.findViewById(R.id.number_ed_txt);
        EditText amount = (EditText) mView.findViewById(R.id.amount_ed_txt);
        MaterialButton payButton = (MaterialButton) mView.findViewById(R.id.payment_button);
        TextView cancel_txt = (TextView) mView.findViewById(R.id.cancel_txt);

        dialog.setContentView(mView);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(AgentWalletActivity.this);

                    JSONObject jsonBody = new JSONObject();

                    jsonBody.put("receiver_phone",number.getText().toString());
                    jsonBody.put("amount", amount.getText().toString());

                    final String mRequestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.PayAmountWallet + getIntent().getStringExtra("cust_id"),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("PAYMENT_VOLLEY", response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        boolean status = jsonObject.getBoolean("status");
                                        String msg = jsonObject.getString("msg");

                                        if (status == true){

                                            final Dialog successDialog = new Dialog(AgentWalletActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                                            View successView = getLayoutInflater().inflate(R.layout.tranasaction_successfull_dialog, null);

                                            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView message = (TextView) successView.findViewById(R.id.msg_txt);

                                            successDialog.setContentView(successView);

                                            message.setText(msg);
                                            successDialog.show();

                                            JSONObject object = jsonObject.getJSONObject("data");

                                        }
                                        else {

                                            final Dialog failedDialog = new Dialog(AgentWalletActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                                            View failedView = getLayoutInflater().inflate(R.layout.trsaction_failed_dialog, null);

                                            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView failedMessage = (TextView) failedView.findViewById(R.id.failed_msg_txt);

                                            failedDialog.setContentView(failedView);

                                            failedMessage.setText(msg);
                                            failedDialog.show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

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
        });

        dialog.show();
    }

}