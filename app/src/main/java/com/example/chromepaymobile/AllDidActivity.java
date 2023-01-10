package com.example.chromepaymobile;

import static android.os.Build.ID;

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
import com.example.chromepaymobile.Adapter.AllDidAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.AllDidModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AllDidActivity extends AppCompatActivity {

    RecyclerView allDidRecycle;
    ArrayList<AllDidModel> allDidList = new ArrayList<>();
    AllDidAdapter allDidAdapter;
    ImageView backImage;
    NestedScrollView nestedScrollView;
    int page = 1;
    int limit = 10;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_did);

        allDidRecycle = findViewById(R.id.all_did_recycle);
        backImage = findViewById(R.id.back_img_all_did);
        nestedScrollView = findViewById(R.id.nested_sv);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AllDidApi(page, limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){

                    page++;
                    AllDidApi(page, limit);
                }
            }
        });
    }

    public void AllDidApi(int page, int limit){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("page",page);

            final String mRequestBody = jsonBody.toString();

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String agentID = sharedPreferences.getString("ID","");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.AllDid+agentID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("ALL_DID_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            JSONArray jsonArray = jsonObject.getJSONArray("filter");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("_id");
                                String DID = object.getString("digitalrefID");
                                String name = object.getString("fullname");
                                String photo = object.getString("IDphoto");
                                String Status = object.getString("status");
                                String email = object.getString("email");
                                String cust_phone = object.getString("phone");



                               AllDidModel allDidModel = new AllDidModel();
                               allDidModel.setId(id);
                               allDidModel.setDID("######"+DID.substring(7,10));
                               allDidModel.setStatus(Status);
                               allDidModel.setName(name);
                               allDidModel.setPhone(cust_phone);
                               allDidModel.setImg(photo);
                               allDidModel.setEmail(email);

                               allDidList.add(allDidModel);
                               allDidAdapter = new AllDidAdapter(allDidList,AllDidActivity.this);
                               allDidRecycle.setAdapter(allDidAdapter);
                               allDidAdapter.notifyDataSetChanged();
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
                            Toast.makeText(AllDidActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    }){
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