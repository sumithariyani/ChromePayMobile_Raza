package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LoanPassDetailActivty extends AppCompatActivity {

    ImageView backImage, profile, nextBtn;
    TextView name, number, orgName, loanType, interest, Emi, totalAmount, durationYear, totalInterestAmount,dueDate,dueAmount;
    MaterialButton passBtn,payEMI;
    String loanID;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_pass_detail_activty);

        backImage = findViewById(R.id.back_img_lpd);
        name = findViewById(R.id.lpd_name);
        profile = findViewById(R.id.image_profile_lpd);
        number = findViewById(R.id.phone_lpd);
        passBtn = findViewById(R.id.pass_button_lpd);
        orgName = findViewById(R.id.org_name_tv_lpd);
        loanType = findViewById(R.id.loan_type_tv_lpd);
        interest = findViewById(R.id.interest_tv_lpd);
        Emi = findViewById(R.id.emi_tv_lpd);
        totalAmount = findViewById(R.id.total_amount_tv_lpd);
        durationYear = findViewById(R.id.duration_tv_lpd);
        totalInterestAmount = findViewById(R.id.tia_tv_lpd);
        nextBtn = findViewById(R.id.ic_next_lpd);
        payEMI = findViewById(R.id.pay_emi_button_lpd);
        dueDate = findViewById(R.id.due_date_tv);
        dueAmount = findViewById(R.id.due_tv);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        name.setText(getIntent().getStringExtra("name"));
        number.setText(getIntent().getStringExtra("number"));
        passBtn.setText(getIntent().getStringExtra("status"));
        orgName.setText(getIntent().getStringExtra("orgName"));
        loanType.setText(getIntent().getStringExtra("loanType"));
        interest.setText(getIntent().getStringExtra("interest"));
        Emi.setText(getIntent().getStringExtra("EMI"));
        totalAmount.setText(getIntent().getStringExtra("totalAmount"));
        durationYear.setText(getIntent().getStringExtra("durationYear"));
        totalInterestAmount.setText(getIntent().getStringExtra("totalInterestAmount"));

        Picasso.get()
                .load(getIntent().getStringExtra("photo"))
                .placeholder(R.drawable.all_dids_06)
                .fit()
                .into(profile);


        payEMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DIALOG();
            }
        });

        DueAmountApi();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoanPassDetailActivty.this, EMIDetailActivity.class);
                intent.putExtra("_ID",getIntent().getStringExtra("_ID"));
                startActivity(intent);
            }
        });

    }

    public void DIALOG(){

        final Dialog dialog = new Dialog(LoanPassDetailActivty.this, android.R.style.Theme_Translucent_NoTitleBar);
        View mView = getLayoutInflater().inflate(R.layout.pay_emi_dialog, null);


        EditText editText = (EditText) mView.findViewById(R.id.amount_et);
        TextView textView = (TextView) mView.findViewById(R.id.amount_text);
        TextView textView2 = (TextView) mView.findViewById(R.id.cancel_text);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialButton button = (MaterialButton) mView.findViewById(R.id.pay_btn);

            dialog.setContentView(mView);

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            textView.setText("Your Amount is "+ dueAmount.getText().toString());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(LoanPassDetailActivty.this);

                        JSONObject jsonBody = new JSONObject();

                        jsonBody.put("Amount",editText.getText().toString());

                        final String mRequestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.PayEmi+getIntent().getStringExtra("_ID"), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("DUE_AMOUNT_VOLLEY", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    boolean status = jsonObject.getBoolean("status");
                                    String msg = jsonObject.getString("msg");

                                    if (status == true) {
                                        Toast.makeText(LoanPassDetailActivty.this, "" + msg, Toast.LENGTH_SHORT).show();

                                        final Dialog successDialog = new Dialog(LoanPassDetailActivty.this, android.R.style.Theme_Translucent_NoTitleBar);
                                        View successView = getLayoutInflater().inflate(R.layout.payment_successfull_dialog, null);

                                        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView dis = (ImageView) successView.findViewById(R.id.dis_dialog_paymenyt_success);
                                        successDialog.setContentView(successView);

                                        dis.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                successDialog.dismiss();
                                                dialog.dismiss();
                                            }
                                        });
                                        successDialog.show();
                                    }
                                    else {
                                        Toast.makeText(LoanPassDetailActivty.this, ""+msg, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(LoanPassDetailActivty.this, ""+error, Toast.LENGTH_SHORT).show();
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
        });

            dialog.show();

    }

    public void DueAmountApi(){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.NextDueAmount+getIntent().getStringExtra("_ID"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("DUE_AMOUNT_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){
                            String nextEMI = jsonObject.getString("Next_EMI");
                            String amount = jsonObject.getString("Amount");

                            dueDate.setText("Next due on " +nextEMI);
                            dueAmount.setText(amount);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(LoanPassDetailActivty.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}