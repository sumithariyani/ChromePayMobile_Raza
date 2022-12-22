package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NewPasswordActivity extends AppCompatActivity {

    EditText newPass, confirmPass;
    ImageView imgToggle1, imgToggle2;
    MaterialButton resetPassWord;
    ImageView backImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        newPass = findViewById(R.id.new_et_pass2);
        confirmPass = findViewById(R.id.confirm_et_pass);
        imgToggle1 = findViewById(R.id.new_img_toggle);
        imgToggle2 = findViewById(R.id.confirm_img_toggle);
        resetPassWord = findViewById(R.id.reset_pass_btn);
        backImage = findViewById(R.id.back_img);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        resetPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPasswordApi();
            }
        });

        imgToggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

                    imgToggle1.setImageResource(R.drawable.hidden);
                    newPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPass.setSelection(newPass.length());
                }
                else {

                    imgToggle1.setImageResource(R.drawable.login_stuff_06);
                    newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPass.setSelection(newPass.length());
                }
            }
        });

        imgToggle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (confirmPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

                    imgToggle2.setImageResource(R.drawable.hidden);
                    confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPass.setSelection(confirmPass.length());
                }
                else {

                    imgToggle2.setImageResource(R.drawable.login_stuff_06);
                    confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPass.setSelection(confirmPass.length());
                }
            }
        });


    }

    public void NewPasswordApi(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("newPassword",newPass.getText().toString());
            jsonBody.put("confirmPassword",confirmPass.getText().toString());
            jsonBody.put("email",getIntent().getStringExtra("email"));

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, AllUrl.ChangePassWord, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("NewPassword_VOLLEY", response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        if (status == true){

                            String msg = jsonObject.getString("msg");
                            Toast.makeText(NewPasswordActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NewPasswordActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(NewPasswordActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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