package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LinkedServiceActivity extends AppCompatActivity {

    Spinner selectOrg;
    String ID;
    ImageView backImage;
    EditText mobileNo;
    MaterialButton saveBtn;
    String otp;
    String number;

    ArrayList<String> organisationList = new ArrayList<>();
    ArrayList<String> organisationListId = new ArrayList<>();
    ArrayAdapter<String> organisationAdapter;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_service);

        backImage = findViewById(R.id.back_img_ls);
        selectOrg = findViewById(R.id.org_select_ls);
        mobileNo = findViewById(R.id.ed_mobile_no);
        saveBtn = findViewById(R.id.save_change_btn);

        mobileNo.setText(getIntent().getStringExtra("phone"));

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        number = mobileNo.getText().toString();
        GetOrgList();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinkedOTP();
            }
        });

    }

    public void GetOrgList(){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, AllUrl.OrganisationList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            JSONArray jsonArray = jsonObject.getJSONArray("Org");
                            organisationList.add("Select Organisation");
                            organisationListId.add("");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("_id");
                                String name = object.getString("name");


                                organisationList.add(name);
                                organisationListId.add(id);

                                organisationAdapter  = new ArrayAdapter<String>(LinkedServiceActivity.this, android.R.layout.simple_spinner_item,organisationList);
                                organisationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                selectOrg.setAdapter(organisationAdapter);


                                selectOrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        System.out.println("ggjkfghfgf"+organisationListId.get(i));
                                        ID = organisationListId.get(i);

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LinkedServiceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LinkedOTP(){
        {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(LinkedServiceActivity.this);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("Phone",mobileNo.getText().toString());

                final String mRequestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.LinkedServicesOTP, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LINKED_OTP_VOLLEY", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean status = jsonObject.getBoolean("status");
                            String msg = jsonObject.getString("msg");

                            if (status == true){
                                Toast.makeText(LinkedServiceActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                Dialog();
                            }else {
                                Toast.makeText(LinkedServiceActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LinkedServiceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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

    public void Dialog(){

        final Dialog dialog = new Dialog(LinkedServiceActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        View mView = getLayoutInflater().inflate(R.layout.verification_dialodg_layout, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView dis = (ImageView) mView.findViewById(R.id.dis_dialog_loan);
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

                //////////////Verify OTP Api/////////////
                {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(LinkedServiceActivity.this);
                        otp = editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString()+editText5.getText().toString()+editText6.getText().toString();

                        JSONObject jsonBody = new JSONObject();

                        jsonBody.put("otp",otp);
                        jsonBody.put("OrgID",ID);
                        jsonBody.put("Phone",mobileNo.getText().toString());

                        final String mRequestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.CustomerLinked, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("CUSTOMER_LINKED_VOLLEY", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    boolean status = jsonObject.getBoolean("status");
                                    String msg = jsonObject.getString("msg");

                                    if (status == true){
                                        Toast.makeText(LinkedServiceActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                       SuccesDialog();
                                    }else {
                                        Toast.makeText(LinkedServiceActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(LinkedServiceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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

    public void SuccesDialog(){

        final Dialog linkDialog = new Dialog(LinkedServiceActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        View linkView = getLayoutInflater().inflate(R.layout.did_success_dialog_layout, null);


        MaterialButton button = (MaterialButton) linkView.findViewById(R.id.ok_btn);
        ImageView imageView = (ImageView) linkView.findViewById(R.id.dis_dialog);

        linkDialog.setContentView(linkView);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LinkedServiceActivity.this, AgentDashActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkDialog.dismiss();
            }
        });

        linkDialog.show();
    }


}