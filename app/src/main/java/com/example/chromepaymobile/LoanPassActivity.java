package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Adapter.LoanPassAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.LoanPassModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoanPassActivity extends AppCompatActivity {

    ImageView backImage;
    RecyclerView recyclerView;
    String token;

    ArrayList<LoanPassModel> passList = new ArrayList<>();
    LoanPassAdapter loanPassAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_pass);

        backImage = findViewById(R.id.back_img_lpa);
        recyclerView = findViewById(R.id.lp_recycle);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
        token = sharedPreferences.getString("token","");

        LoanPass();

    }


    public void LoanPass(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.LoanPass + token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            JSONArray jsonArray = jsonObject.getJSONArray("find");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                JSONObject org = object.getJSONObject("OrganisationID");

                                JSONObject cust = object.getJSONObject("CustomerID");

                                String orgName = org.getString("name");

                                String photo = cust.getString("IDphoto");
                                String name = cust.getString("fullname");
                                String phone = cust.getString("phone");
                                String digitalID = cust.getString("digitalID");

                                String _id = object.getString("_id");
                                String Loan_status = object.getString("Loan_status");
                                String createdAt = object.getString("createdAt");
                                String Loan_type = object.getString("Loan_type");
                                String Interest_Rate = object.getString("Interest_Rate");
                                String EMI = object.getString("EMI");
                                String Total_Amount = object.getString("Total_Amount");
                                String Duration_Year = object.getString("Duration_Year");
                                String Total_Interest_Amount = object.getString("Total_Interest_Amount");

                                System.out.println(" Loan ID "+_id);
                                LoanPassModel loanPassModel = new LoanPassModel();

                                loanPassModel.setName(name);
                                loanPassModel.setPhoto(photo);
                                loanPassModel.setStatus(Loan_status);
                                loanPassModel.setPhone(phone);
                                loanPassModel.setDate(createdAt.substring(0,10));
                                loanPassModel.setOrgName(orgName);
                                loanPassModel.setLoanType(Loan_type);
                                loanPassModel.setId(_id);
                                loanPassModel.setInterest(Interest_Rate);
                                loanPassModel.setEMI(EMI);
                                loanPassModel.setTotalAmount(Total_Amount);
                                loanPassModel.setDurationYear(Duration_Year);
                                loanPassModel.setTotalInterest(Total_Interest_Amount);

                                passList.add(loanPassModel);

                                loanPassAdapter = new LoanPassAdapter(passList,LoanPassActivity.this);
                                recyclerView.setAdapter(loanPassAdapter);
                                loanPassAdapter.notifyDataSetChanged();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoanPassActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}