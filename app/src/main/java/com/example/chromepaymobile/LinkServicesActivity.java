package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Adapter.OrgLinkedAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.OrgLinkedModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LinkServicesActivity extends AppCompatActivity {

    TextView fuseId,wallet;
    ImageView backImage;
    String fuse_wallet_address, Chrome_wallet, name;
    RecyclerView linkeRecyclerView;

    ArrayList<OrgLinkedModel> linkedList = new ArrayList<>();
    OrgLinkedAdapter orgLinkedAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_services);

        backImage = findViewById(R.id.back_img_linkServices);
        linkeRecyclerView = findViewById(R.id.link_recycle);
        wallet = findViewById(R.id.wallet_id);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinkedApi();

    }

    public void LinkedApi(){

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("CustomerPreferences",MODE_PRIVATE);
            String token = sharedPreferences.getString("token","");

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, AllUrl.ORGLinked + getIntent().getStringExtra("cust_id"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("LINKE_VOLLEY", response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");


                        Chrome_wallet = jsonObject.getString("Chrome_wallet");
                        fuse_wallet_address = jsonObject.getString("fuse_wallet_address");


                        if (status == true){

                            wallet.setText(Chrome_wallet);
                            System.out.println(Chrome_wallet);


                            JSONArray jsonArray = jsonObject.getJSONArray("find_orgs_data");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                name = object.getString("name");
                                String logo = object.getString("logo");

                                OrgLinkedModel orgLinkedModel = new OrgLinkedModel();
                                orgLinkedModel.setLogo(logo);
                                orgLinkedModel.setName(name);
                                linkedList.add(orgLinkedModel);

                                orgLinkedAdapter = new OrgLinkedAdapter(linkedList, LinkServicesActivity.this);
                                linkeRecyclerView.setAdapter(orgLinkedAdapter);
                                orgLinkedAdapter.notifyDataSetChanged();


                            }
                        }
                    }catch (Exception e){
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}