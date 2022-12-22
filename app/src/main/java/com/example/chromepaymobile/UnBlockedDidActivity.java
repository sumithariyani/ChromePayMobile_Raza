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
import com.example.chromepaymobile.Adapter.UnBlockedDidAdpter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.UnBlockedDidModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UnBlockedDidActivity extends AppCompatActivity {

    RecyclerView blockRecycle;
    ImageView backImage;

    ArrayList<UnBlockedDidModel> blockList = new ArrayList<>();
    UnBlockedDidAdpter blockedDidAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_blocked_did);

        blockRecycle = findViewById(R.id.blocked_did_recycle);
        backImage = findViewById(R.id.back_img_blocked_did);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        BlockedDIDApi();
    }

    public void BlockedDIDApi(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String agentID = sharedPreferences.getString("ID","");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.BlockedDid + agentID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("BLOCKED_DID_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            JSONArray jsonArray = jsonObject.getJSONArray("findCust");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("_id");
                                String name = object.getString("fullname");
                                String photo = object.getString("IDphoto");
                                int phone = object.getInt("phone");

                                System.out.println(id);
                                UnBlockedDidModel blockedDidModel = new UnBlockedDidModel();
                                blockedDidModel.setId(id);
                                blockedDidModel.setName(name);
                                blockedDidModel.setPhone(String.valueOf(phone));
                                blockedDidModel.setPhoto(photo);


                                blockList.add(blockedDidModel);
                                blockedDidAdpter = new UnBlockedDidAdpter(blockList, UnBlockedDidActivity.this);
                                blockRecycle.setAdapter(blockedDidAdpter);
                                blockedDidAdpter.notifyDataSetChanged();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UnBlockedDidActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}