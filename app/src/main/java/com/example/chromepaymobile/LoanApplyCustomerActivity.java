package com.example.chromepaymobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.example.chromepaymobile.Adapter.LoanApplyAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.LoanApplyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoanApplyCustomerActivity extends AppCompatActivity {

    RecyclerView loanApplyRecycle;
    String token;
    NestedScrollView nestedScrollView;
    int page = 0;
    int limit = 10;
    ImageView backImage;

    ArrayList<LoanApplyModel> pendingList = new ArrayList<>();
    LoanApplyAdapter loanApplyAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_apply_customer);

        loanApplyRecycle = findViewById(R.id.lac_recycle);
        nestedScrollView = findViewById(R.id.nested_sv_lac);
        backImage = findViewById(R.id.back_img_lac);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
        token = sharedPreferences.getString("token","");

        CustomerLoanApply(page, limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){

                    page++;
                    CustomerLoanApply(page, limit);
                }
            }
        });

    }

    public void CustomerLoanApply(int page, int limit){
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("page",page);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.CustomerLoanApply+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOAN_APPLY_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");


                        if (status == true){

                            JSONArray find_Loans = jsonObject.getJSONArray("find_Loans");

                            for (int i=0; i<find_Loans.length(); i++){

                                JSONObject object = find_Loans.getJSONObject(i);

                                JSONObject CustomerID = object.getJSONObject("CustomerID");

                                JSONObject org = object.getJSONObject("OrganisationID");

                                String Loan_type = object.getString("Loan_type");
                                String Interest_Rate = object.getString("Interest_Rate");
                                String EMI = object.getString("EMI");
                                String Total_Amount = object.getString("Total_Amount");
                                String Duration_Year = object.getString("Duration_Year");
                                String Total_Interest_Amount = object.getString("Total_Interest_Amount");
                                String createdAt = object.getString("createdAt");

                                String orgName = org.getString("name");

                                String name = CustomerID.getString("fullname");
                                String photo = CustomerID.getString("IDphoto");
                                int phone = CustomerID.getInt("phone");
                                String digitalID = CustomerID.getString("digitalID");
                                String Loan_status = object.getString("Loan_status");

                                LoanApplyModel loanApplyModel = new LoanApplyModel();
                                loanApplyModel.setName(name);
                                loanApplyModel.setNumber(String.valueOf(phone));
                                loanApplyModel.setDate(createdAt.substring(0,10));
                                loanApplyModel.setPhoto(photo);
                                loanApplyModel.setStatus(Loan_status);
                                loanApplyModel.setOrganisationName(orgName);
                                loanApplyModel.setLoanType(Loan_type);
                                loanApplyModel.setInterest(Interest_Rate);
                                loanApplyModel.setEMI(EMI);
                                loanApplyModel.setTotalAmount(Total_Amount);
                                loanApplyModel.setDurationYear(Duration_Year);
                                loanApplyModel.setTotalInterestAmount(Total_Interest_Amount);
                                pendingList.add(loanApplyModel);

                                loanApplyAdapter = new LoanApplyAdapter(pendingList,LoanApplyCustomerActivity.this);
                                loanApplyRecycle.setAdapter(loanApplyAdapter);
                                loanApplyAdapter.notifyDataSetChanged();
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
                            Toast.makeText(LoanApplyCustomerActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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