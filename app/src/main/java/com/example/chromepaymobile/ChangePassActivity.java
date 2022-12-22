package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ChangePassActivity extends AppCompatActivity {

    EditText oldPass,newPass,confirmPass;
    MaterialButton updatePass;
    ImageView backImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        oldPass = findViewById(R.id.et_old_pass);
        newPass = findViewById(R.id.et_new_pass);
        confirmPass = findViewById(R.id.et_confirm_pass);
        updatePass = findViewById(R.id.update_pass);
        backImage = findViewById(R.id.back_img_ch_pass);

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CHANGE_PASS_API();
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void CHANGE_PASS_API(){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("oldPassword",oldPass.getText().toString());
            jsonBody.put("newPassword",newPass.getText().toString());
            jsonBody.put("confirmPassword",confirmPass.getText().toString());

            final String mRequestBody = jsonBody.toString();

            System.out.println(mRequestBody);

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String agentID = sharedPreferences.getString("ID","");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.ChangeAgentPass+agentID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("LOG_CHANGE_PASS_AGENT_VOLLEY"+response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");
                        String msg = jsonObject.getString("msg");

                        if (status == true){
                            Toast.makeText(ChangePassActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(ChangePassActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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