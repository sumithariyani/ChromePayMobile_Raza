package com.example.chromepaymobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.slider.Slider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoanRequestActivity extends AppCompatActivity {

    Slider amountSlider, monthSlider;
    TextView amountTv, monthTv, interestPercent;
    MaterialButton countinueBtn;
    String loanOtp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_request2);

        amountSlider = findViewById(R.id.amount_slider);
        monthSlider = findViewById(R.id.moth_slider);
        amountTv = findViewById(R.id.loan_amount_tv);
        monthTv = findViewById(R.id.loan_month_tv);
        countinueBtn = findViewById(R.id.continue_btn);
        interestPercent = findViewById(R.id.interest_perc_tv);

        amountSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                amountTv.setText(""+value);
            }
        });

        monthSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                monthTv.setText(""+value+"month");
            }
        });

        GetLoanInterest();

        countinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendOtpApi();
            }
        });

    }

    public void GetLoanInterest(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("LoanType", getIntent().getStringExtra("loan_type"));

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.GetLoanInterest+getIntent().getStringExtra("orgId"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("INTEREST_RATE_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        String msg = jsonObject.getString("msg");

                        if (status == true){

                            JSONObject object = jsonObject.getJSONObject("item");

                            String id = object.getString("_id");
                            int name = object.getInt("Home_Loans");

                            interestPercent.setText(name+"%");

                        }else {
                            Toast.makeText(LoanRequestActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                        System.out.println(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoanRequestActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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


    public void SendOtpApi(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.LoanOTP+getIntent().getStringExtra("custID"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("SEND_OTP_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("statsu");

                        if (status == true){
                            String msg = jsonObject.getString("msg");

                            Dialog();

                            Toast.makeText(LoanRequestActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoanRequestActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Dialog() {

        final Dialog dialog = new Dialog(LoanRequestActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        View mView = getLayoutInflater().inflate(R.layout.verification_dialog_layout_loan, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView dis = (ImageView) mView.findViewById(R.id.dis_dialog);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialButton verify = (MaterialButton) mView.findViewById(R.id.verify_did_btn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editText1 = (EditText) mView.findViewById(R.id.editText1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editText2 = (EditText) mView.findViewById(R.id.editText2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editText3 = (EditText) mView.findViewById(R.id.editText3);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})EditText editText4 = (EditText) mView.findViewById(R.id.editText4);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editText5 = (EditText) mView.findViewById(R.id.editText5);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editText6 = (EditText) mView.findViewById(R.id.editText6);
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

                try {

                    String Otp = editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString()+editText5.getText().toString()+editText6.getText().toString();

                    RequestQueue requestQueue = Volley.newRequestQueue(LoanRequestActivity.this);

                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
                    String agentID = sharedPreferences.getString("ID","");
                    String token = sharedPreferences.getString("token","");

                    System.out.println("hghghgjghgjjgjh"+token);
                    JSONObject jsonBody = new JSONObject();

                    System.out.println("loanAct"+amountTv.getText().toString());

                    jsonBody.put("Interest",interestPercent.getText().toString());
                    jsonBody.put("Amount",amountTv.getText().toString());
                    jsonBody.put("Emi_Months",monthTv.getText().toString());
                    jsonBody.put("orgID",getIntent().getStringExtra("orgId"));
                    jsonBody.put("custID",getIntent().getStringExtra("custID"));
                    jsonBody.put("Loan_type",getIntent().getStringExtra("loan_type"));
                    jsonBody.put("otp",Otp);

                    final String mRequestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.CalculateAmount+token, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("CALCULATE_AMOUNT_VOLLEY", response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                boolean status = jsonObject.getBoolean("status");

                                if (status == true){

                                    String msg = jsonObject.getString("msg");

                                    Toast.makeText(LoanRequestActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                    JSONObject obj = jsonObject.getJSONObject("obj");

                                    final Dialog sureDialog = new Dialog(LoanRequestActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                                    View dialogView = getLayoutInflater().inflate(R.layout.confirmation_dialog_layout, null);

                                    MaterialButton yesBtn = (MaterialButton) dialogView.findViewById(R.id.yes_btn);

                                    sureDialog.setContentView(dialogView);


                                    yesBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent intent = new Intent(LoanRequestActivity.this,LoanApplyCustomerActivity.class);
                                            intent.putExtra("custId",getIntent().getStringExtra("custID"));
                                            startActivity(intent);
                                        }
                                    });
                                    sureDialog.show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(LoanRequestActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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