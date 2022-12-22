package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Adapter.EMIAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.EMIModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EMIDetailActivity extends AppCompatActivity {

    ImageView backImage;
    RecyclerView emiRecycle;

    ArrayList<EMIModel> emiList = new ArrayList<>();
    EMIAdapter emiAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emidetail);

        backImage = findViewById(R.id.back_img_emi_d);
        emiRecycle = findViewById(R.id.emi_recycle);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        InstallmentDetail();
    }

    public void InstallmentDetail(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.Installment+getIntent().getStringExtra("_ID"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("EMI_DETAIL_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            JSONArray jsonArray = jsonObject.getJSONArray("Installments");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String Installment_No = object.getString("Installment_No");
                                String Installment_Pay_Amount = object.getString("Installment_Pay_Amount");
                                String Pay_Date = object.getString("Pay_Date");
                                String Installment_Date = object.getString("Installment_Date");
                                String Installment_Status = object.getString("status");
                                String _id = object.getString("_id");

                                EMIModel emiModel = new EMIModel();

                                emiModel.setInstallmentNo(Installment_No);
                                emiModel.setInstallmentPayAmount(Installment_Pay_Amount);
                                emiModel.setPayDate(Pay_Date.substring(0,10));
                                emiModel.setInstallmentDate(Installment_Date.substring(0,10));
                                emiModel.setStatus(Installment_Status);
                                emiModel.setId(_id);

                                emiList.add(emiModel);

                                emiAdapter = new EMIAdapter(emiList,EMIDetailActivity.this);
                                emiRecycle.setAdapter(emiAdapter);
                                emiAdapter.notifyDataSetChanged();
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
                            Toast.makeText(EMIDetailActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}