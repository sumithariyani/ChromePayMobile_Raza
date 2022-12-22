package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EditProfileActivity extends AppCompatActivity {

    EditText fullName,phoneNumber,email,address,city,country,postCode;
    MaterialButton updateProfileBtn;
    String agentID;
    ImageView backImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullName = findViewById(R.id.ed_full_name);
        phoneNumber = findViewById(R.id.ed_mobile_no);
        email = findViewById(R.id.ed_email);
        address = findViewById(R.id.ed_address);
        city = findViewById(R.id.ed_city);
        country = findViewById(R.id.ed_country);
        postCode = findViewById(R.id.ed_postCode);
        updateProfileBtn = findViewById(R.id.update_profile_btn);
        backImage = findViewById(R.id.back_img_et_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
        agentID = sharedPreferences.getString("ID","");

        AgentProfileApi();

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AgentProfileUpdate();
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void AgentProfileApi(){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, AllUrl.AgentProfile + agentID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("AGENT_PROFILE_VOLLEY", response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                boolean status = jsonObject.getBoolean("status");

                                if (status == true){

                                    JSONObject filter = jsonObject.getJSONObject("filter");

                                    String ID = filter .getString("_id");
                                    String Name = filter .getString("name");
                                    String Email = filter .getString("email");
                                    String Password = filter .getString("password");
                                    String Phone = filter .getString("phone");
                                    String Country = filter .getString("country");
                                    String Address = filter .getString("address");
                                    String City = filter .getString("city");
                                    String PostCode = filter .getString("postCode");
                                    String TransactionLimit = filter .getString("transectionLimit");

                                    fullName.setText(Name);
                                    phoneNumber.setText(Phone);
                                    email.setText(Email);
                                    address.setText(Address);
                                    city.setText(City);
                                    country.setText(Country);
                                    postCode.setText(PostCode);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EditProfileActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AgentProfileUpdate(){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name",fullName.getText().toString());
            jsonBody.put("email",email.getText().toString());
            jsonBody.put("phone",phoneNumber.getText().toString());
            jsonBody.put("country",country.getText().toString());
            jsonBody.put("address",address.getText().toString());
            jsonBody.put("city",city.getText().toString());
            jsonBody.put("postCode",postCode.getText().toString());

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.UpdateProfile+agentID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("AGENT_UPDATE_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            String msg = jsonObject.getString("msg");
                            Toast.makeText(EditProfileActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EditProfileActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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