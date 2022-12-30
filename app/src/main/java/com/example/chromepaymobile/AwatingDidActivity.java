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
import com.example.chromepaymobile.Adapter.AllDidAdapter;
import com.example.chromepaymobile.Adapter.AwatingDidAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.AllDidModel;
import com.example.chromepaymobile.Models.AwatingDIDModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AwatingDidActivity extends AppCompatActivity {

    RecyclerView awatingRecycle;
    ImageView backImage;
    TextView noData;
    NestedScrollView nestedScrollView;
    int page = 2;
    int limit = 10;

    AwatingDidAdapter awatingDidAdapter;
    ArrayList<AwatingDIDModel> awatingList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awating_did);

        awatingRecycle = findViewById(R.id.awating_did_recycle);
        backImage = findViewById(R.id.back_img_awating_did);
        nestedScrollView = findViewById(R.id.ns);
        noData = findViewById(R.id.nodata);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AwatingDidApi(page, limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == v.getChildAt(2).getMeasuredHeight()-v.getMeasuredHeight()){

                    page++;
                    AwatingDidApi(page,limit);
                }
            }
        });

    }

    public void AwatingDidApi(int page, int limit){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("page",page);

            final String mRequestBody = jsonBody.toString();

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String token = sharedPreferences.getString("token","");
            System.out.println("123544"+token);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.AwatingDid + token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("AWATING_DID_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("statussss");

                        if (status == true){

                            JSONArray jsonArray = jsonObject.getJSONArray("filter");

                            if (jsonArray.length()==0){
                                noData.setText("No Pending Approval");
                                noData.setVisibility(View.VISIBLE);
                            }
                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("_id");
                                String DID = object.getString("digitalrefID");
                                String name = object.getString("fullname");
                                String photo = object.getString("IDphoto");
                                String  phone = object.getString("phone");

                                AwatingDIDModel awatingDIDModel = new AwatingDIDModel();
                                awatingDIDModel.setId(id);
                                awatingDIDModel.setDid("######"+DID.substring(7,10));
                                awatingDIDModel.setName(name);
                                awatingDIDModel.setPhone(String.valueOf(phone));
                                awatingDIDModel.setPhoto(photo);

                                awatingList.add(awatingDIDModel);
                                awatingDidAdapter = new AwatingDidAdapter(awatingList,AwatingDidActivity.this);
                                awatingRecycle.setAdapter(awatingDidAdapter);
                                awatingDidAdapter.notifyDataSetChanged();
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
                            Toast.makeText(AwatingDidActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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