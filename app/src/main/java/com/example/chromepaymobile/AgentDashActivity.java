package com.example.chromepaymobile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Adapter.ADBRecycleAdapter;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Models.ADBRecycleModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AgentDashActivity extends AppCompatActivity {

    RelativeLayout card;
    RecyclerView adbRecycle;
    TextView name;

    ArrayList<ADBRecycleModel> adbList = new ArrayList<>();
    ADBRecycleAdapter adbRecycleAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_dash);

        card = findViewById(R.id.card);
        adbRecycle = findViewById(R.id.abd_recycle);
        name = findViewById(R.id.name_tv);

        AgentDashApi();

        adbList.add(new ADBRecycleModel("Active D-IDs",R.drawable.agent_dashboard_stuff_03));
        adbList.add(new ADBRecycleModel("Pending approval",R.drawable.agent_dashboard_stuff_08));
        adbList.add(new ADBRecycleModel("Agent Commission",R.drawable.agent_dashboard_stuff_10));
        adbList.add(new ADBRecycleModel("Agent Performance",R.drawable.agent_dashboard_stuff_10));
        adbList.add(new ADBRecycleModel("Agency Banking",R.drawable.icon_12));
        adbList.add(new ADBRecycleModel("Settings",R.drawable.agent_dashboard_stuff_11));

        adbRecycleAdapter = new ADBRecycleAdapter(adbList,AgentDashActivity.this);
        adbRecycle.setAdapter(adbRecycleAdapter);
        adbRecycleAdapter.notifyDataSetChanged();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AgentDashActivity.this,RegisterCustomer1Activity.class);
                startActivity(intent);
            }
        });
    }

    public void AgentDashApi(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String agentId = sharedPreferences.getString("ID","");

            StringRequest stringRequest = new StringRequest(Request.Method.GET, AllUrl.AgentDashInfo+agentId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("AGENT_DASH_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");
                        String NumberOFUser = jsonObject.getString("NumberOFUser");
                        String agentName = jsonObject.getString("agentName");
                        String email = jsonObject.getString("email");
                        String country = jsonObject.getString("country");
                        int mobile = jsonObject.getInt("mobile");
                        String totalTransection = jsonObject.getString("totalTransection");

                        if (status == true) {

                            name.setText(agentName);

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