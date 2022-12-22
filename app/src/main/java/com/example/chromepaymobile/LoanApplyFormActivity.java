package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.chromepaymobile.Adapter.TypeLoanAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.TypesLoanModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LoanApplyFormActivity extends AppCompatActivity {

    Spinner selectOrg;
    String ob;
    TextView custName, custEmail;
    ImageView backImage,profile;
    String ID;
    String custID;
    RecyclerView typeRecycle;

    ArrayList<TypesLoanModel> typeList = new ArrayList<>();
    TypeLoanAdapter typeLoanAdapter;

    ArrayList<String> organisationList = new ArrayList<>();
    ArrayList<String> organisationListId = new ArrayList<>();
    ArrayAdapter<String> organisationAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_apply_form);

        selectOrg = findViewById(R.id.org_select);
        custName = findViewById(R.id.loan_cust_name);
        custEmail = findViewById(R.id.email_loan);
        backImage = findViewById(R.id.back_img_add_loan);
        profile = findViewById(R.id.image_profile_loan);
        typeRecycle = findViewById(R.id.type_loan_recycle);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        custID = getIntent().getStringExtra("cust_id");

        custName.setText(getIntent().getStringExtra("name"));
        custEmail.setText(getIntent().getStringExtra("email"));

        Picasso.get()
                        .load(getIntent().getStringExtra("photo"))
                                .placeholder(R.drawable.all_dids_06)
                                        .fit()
                                                .into(profile);



        GetOrgApi();

    }

    public void GetOrgApi(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.GetOrgApi+getIntent().getStringExtra("cust_id"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("GET_ORG_VOLLEY", response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true) {

                            JSONArray jsonArray = jsonObject.getJSONArray("final");
                            organisationList.add("Select Organisation");
                            organisationListId.add("");

                            for (int i=0; i<jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("_id");
                                String name = object.getString("name");


                                organisationList.add(name);
                                organisationListId.add(id);

                                organisationAdapter  = new ArrayAdapter<String>(LoanApplyFormActivity.this, android.R.layout.simple_spinner_item,organisationList);
                                organisationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                selectOrg.setAdapter(organisationAdapter);

                                selectOrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        System.out.println("ggjkfghfgf"+organisationListId.get(i));
                                        ID = organisationListId.get(i);

                                        GetLoanApi();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                                System.out.println(id);
                            }
                            System.out.println("././/.././." + jsonArray);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoanApplyFormActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetLoanApi(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            System.out.println("jkghIDID"+ID);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.GetLoanApi+ID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("GET_LOAN_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("staus");

                        if (status == true){

                            JSONArray values = jsonObject.getJSONArray("values");


                            for (int i=0; i<values.length(); i++){

                                ob = values.getString(i);

                                TypesLoanModel typesLoanModel = new TypesLoanModel();
                                typesLoanModel.setName(ob);

                                typeList.add(typesLoanModel);

                                System.out.println("activity"+custID);
                                typeLoanAdapter = new TypeLoanAdapter(typeList,LoanApplyFormActivity.this,ID,custID);
                                typeRecycle.setAdapter(typeLoanAdapter);
                                typeLoanAdapter.notifyDataSetChanged();
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
                        }
                    });

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}