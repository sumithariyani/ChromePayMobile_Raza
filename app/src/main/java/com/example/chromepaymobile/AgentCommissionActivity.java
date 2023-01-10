package com.example.chromepaymobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.chromepaymobile.Adapter.AgentCommissionAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.AgentCommissionModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

public class AgentCommissionActivity extends AppCompatActivity {

    ImageView backImage;
    RecyclerView commissionRecycle;
    TextView fromDate,toDate,noData;
    NestedScrollView nestedScrollView;
    int year;
    int month;
    int day;
    LinearLayout btn;
    int page = 0;
    int limit = 10;

    ArrayList<AgentCommissionModel> agentList = new ArrayList<>();
    AgentCommissionAdapter agentCommissionAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_commission);

        backImage = findViewById(R.id.back_img_ac);
        commissionRecycle = findViewById(R.id.agent_commission_recycle);
        fromDate = findViewById(R.id.from_date_et);
        toDate = findViewById(R.id.to_date_tv);
        btn = findViewById(R.id.date_sub_btn);
        noData = findViewById(R.id.nodata);
        nestedScrollView = findViewById(R.id.NestedSV);
        final Calendar calendar = Calendar.getInstance();

        AgentCommission(page,limit);
        AgentCommissionNodate(page, limit);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AgentCommissionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        fromDate.setText(i+"/"+(i1+1)+"/"+i2);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AgentCommission(page, limit);

            }
        });


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){

                    page++;
                    AgentCommission(page,limit);
                    AgentCommissionNodate(page,limit);
                }
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AgentCommissionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        toDate.setText(i+"/"+(i1+1)+"/"+i2);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }
    
    public void AgentCommission(int page, int limit){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fromDate",fromDate.getText().toString());
            jsonBody.put("toDate",toDate.getText().toString());
            jsonBody.put("page",page);

            System.out.println("fromDate"+fromDate.getText().toString());
            System.out.println("toDate"+toDate.getText().toString());
            final String mRequestBody = jsonBody.toString();

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String token = sharedPreferences.getString("token","");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.AgentCommission+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("AGENT_COMMISSION_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        JSONArray filter = jsonObject.getJSONArray("filter");
                        if (status == true){

                            if (filter.length() == 0){
                                noData.setText("No data found");
                                noData.setVisibility(View.VISIBLE);
                                commissionRecycle.setVisibility(View.GONE);
                                agentCommissionAdapter.notifyDataSetChanged();
                            }else {
                                commissionRecycle.setVisibility(View.VISIBLE);
                                agentCommissionAdapter.notifyDataSetChanged();
                                noData.setVisibility(View.GONE);

                            }
                            for (int i=0; i<filter.length(); i++){
                                try {
                                    JSONObject object = filter.getJSONObject(i);

                                    String _id = object.getString("_id");
                                    String custPhoto = object.getString("custPhoto");
                                    String agentID = object.getString("agentID");
                                    String custID = object.getString("custID");
                                    String custName = object.getString("custName");
                                    String commission = object.getString("commission");
                                    String createdAt = object.getString("createdAt");

                                    AgentCommissionModel agentCommissionModel = new AgentCommissionModel();
                                    agentCommissionModel.setImg(custPhoto);
                                    agentCommissionModel.setName(custName);
                                    agentCommissionModel.setCommission(commission);
                                    agentCommissionModel.setDate(createdAt.substring(0,10));
                                    agentList.add(agentCommissionModel);
                                    agentCommissionAdapter = new AgentCommissionAdapter(agentList,AgentCommissionActivity.this);
                                    commissionRecycle.setAdapter(agentCommissionAdapter);
                                    agentCommissionAdapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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

                            Toast.makeText(AgentCommissionActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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

    public void AgentCommissionNodate(int page, int limit){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("page",page);

            final String mRequestBody = jsonBody.toString();

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String token = sharedPreferences.getString("token","");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.AgentCommission+token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("AGENT_COMMISSION_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        JSONArray filter = jsonObject.getJSONArray("filter");
                        if (status == true){

                            for (int i=0; i<filter.length(); i++){
                                try {
                                    JSONObject object = filter.getJSONObject(i);

                                    String _id = object.getString("_id");
                                    String custPhoto = object.getString("custPhoto");
                                    String agentID = object.getString("agentID");
                                    String custID = object.getString("custID");
                                    String custName = object.getString("custName");
                                    String commission = object.getString("commission");
                                    String createdAt = object.getString("createdAt");

                                    AgentCommissionModel agentCommissionModel = new AgentCommissionModel();
                                    agentCommissionModel.setImg(custPhoto);
                                    agentCommissionModel.setName(custName);
                                    agentCommissionModel.setCommission(commission);
                                    agentCommissionModel.setDate(createdAt.substring(0,10));
                                    agentList.add(agentCommissionModel);
                                    agentCommissionAdapter = new AgentCommissionAdapter(agentList,AgentCommissionActivity.this);
                                    commissionRecycle.setAdapter(agentCommissionAdapter);
                                    agentCommissionAdapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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

                            Toast.makeText(AgentCommissionActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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