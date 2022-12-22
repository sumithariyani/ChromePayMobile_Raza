package com.example.chromepaymobile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ImageView back,imgToggle;
    MaterialButton login;
    EditText etEmail,etPass;
    TextView forgotPassWord;

    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isReadPermissionGranted = false;
    private boolean isWritePermissionGranted = false;
    private boolean isLocationFinePermissionGranted = false;
    private boolean isLocationCoarsePermissionGranted = false;
    private boolean isCameraPermissionGranted = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.back_img);
        login = findViewById(R.id.login);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_pass);
        imgToggle = findViewById(R.id.img_toggle);
        forgotPassWord = findViewById(R.id.forgot_pass_tv);


        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {

                if (result.get(Manifest.permission.READ_EXTERNAL_STORAGE) != null){

                    isReadPermissionGranted = result.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                }

                if (result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != null){

                    isReadPermissionGranted = result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null){

                    isLocationFinePermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                if (result.get(Manifest.permission.ACCESS_COARSE_LOCATION) != null){

                    isLocationCoarsePermissionGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                }

                if (result.get(Manifest.permission.CAMERA) != null){

                    isCameraPermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
        });

        requestPermission();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        forgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,VerifyEmailActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* String emailToText = etEmail.getText().toString();
                String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


                if (!emailToText.matches(emailPattern)){

                    Toast.makeText(LoginActivity.this, "Please Enter valid Email address !", Toast.LENGTH_SHORT).show();
                }*/
                Login_Api();
           /*
                else if (etPass.getText().toString().trim().isEmpty()){

                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

                }*/
            }
        });

        imgToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){

                    imgToggle.setImageResource(R.drawable.invisible);
                    etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPass.setSelection(etPass.length());
                }
                else {

                    imgToggle.setImageResource(R.drawable.login_stuff_06);
                    etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPass.setSelection(etPass.length());
                }
            }
        });
    }


    public void Login_Api(){

        try {

            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", etEmail.getText().toString());
            jsonBody.put("password", etPass.getText().toString());
            final String mRequestBody = jsonBody.toString();

            System.out.println("jsonBody" +jsonBody);

            StringRequest sr = new StringRequest(Request.Method.POST, AllUrl.Login, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOGIN_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        System.out.println("loginApi  responce  >>>>>>>>   "+response);

                        boolean status = jsonObject.getBoolean("status");
                        String msg = jsonObject.getString("msg");


                        if (status == true){

                            String loginStatus = jsonObject.getString("Login_status");

                            if (loginStatus.matches("agent")) {

                                String token = jsonObject.getString("token");
                                String id = jsonObject.getString("ID");
                                String orgId = jsonObject.getString("orgID");

                                Intent intent = new Intent(LoginActivity.this, AgentDashActivity.class);
                                startActivity(intent);
                                finish();

                                SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("ID",id);
                                editor.putString("orgID",orgId);
                                editor.putString("token",token);
                                editor.putBoolean("isLogin",true);
                                editor.commit();
                            }

                            if (loginStatus.matches("customer")){

                                String token = jsonObject.getString("token");
                                String custId = jsonObject.getString("custID");

                                SharedPreferences sharedPreferences = getSharedPreferences("CustomerPreferences",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("token",token);
                                editor.putString("custID",custId);
                                editor.putBoolean("isLoginCust",true);
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                                startActivity(intent);
                                finish();
                            }

//                            String br = getDecodedJwt(token);
//                            System.out.println(msg);
//                            System.out.println(token);
//                            System.out.println(br);
//                            System.out.println(id);
//                            System.out.println(orgId);



                            Toast.makeText(LoginActivity.this, ""+msg, Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(LoginActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOGIN_VOLLEY_ERROR", error.toString());
                            Toast.makeText(LoginActivity.this, ""+error, Toast.LENGTH_LONG).show();

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

            queue.add(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getDecodedJwt(String jwt){

        StringBuilder result = new StringBuilder();
        String[] parts = jwt.split("[.]");
        try{

            int index = 0;
            for(String part: parts){

                if (index >= 2)
                    break;
                index++;
                byte[] decodedBytes = Base64.decode(part.getBytes("UTF-8"), Base64.URL_SAFE);
                result.append(new String(decodedBytes, "UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private void requestPermission(){

        isReadPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        isWritePermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        isLocationFinePermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        isLocationCoarsePermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<String>();

        if (!isReadPermissionGranted){

            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!isWritePermissionGranted){

            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!isLocationFinePermissionGranted){

            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!isLocationCoarsePermissionGranted){

            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!isCameraPermissionGranted){

            permissionRequest.add(Manifest.permission.CAMERA);
        }

        if (!permissionRequest.isEmpty()){

            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

}