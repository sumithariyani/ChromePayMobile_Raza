package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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
import com.example.chromepaymobile.Adapter.CustomerTransferAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.CustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerTransfers extends AppCompatActivity {

    ImageView backImage;
    RecyclerView recyclerView;

    ArrayList<CustomerModel> list = new ArrayList<>();
    CustomerTransferAdapter customerTransferAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_transfers);

        backImage = findViewById(R.id.back_img_ac);
        recyclerView = findViewById(R.id.customer_transfer_recycle);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        Transfer();

    }

    public void Transfer(){


        SharedPreferences sharedPreferences = getSharedPreferences("CustomerPreferences",MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.CustomerTransfers+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("CUST_TRANS_VOLLEY", response);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            JSONArray jsonArray = jsonObject.getJSONArray("transecions");

                            if (jsonArray.length() == 0){
                                Toast.makeText(CustomerTransfers.this, "No have transfers", Toast.LENGTH_SHORT).show();
                            }
                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String name = object.getString("beneficiaryName");
                                String amount = object.getString("receiverAmount");

                                CustomerModel customerModel = new CustomerModel();
                                customerModel.setAmount(amount);
                                customerModel.setName(name);
                                list.add(customerModel);

                                customerTransferAdapter = new CustomerTransferAdapter(list, CustomerTransfers.this);
                                recyclerView.setAdapter(customerTransferAdapter);
                                customerTransferAdapter.notifyDataSetChanged();
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
                            Toast.makeText(CustomerTransfers.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(stringRequest);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}