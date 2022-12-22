package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class OpenAppActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_app);


        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
        boolean Login = sharedPreferences.getBoolean("isLogin",false);

        SharedPreferences customerPreferences = getSharedPreferences("CustomerPreferences",MODE_PRIVATE);
        boolean LoginCust = customerPreferences.getBoolean("isLoginCust",false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Login){
                    Intent intent = new Intent(OpenAppActivity.this,AgentDashActivity.class);
                    startActivity(intent);
                    finish();
                }else if (LoginCust){
                    Intent intent = new Intent(OpenAppActivity.this,CustomerDashBoardActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(OpenAppActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },2000);

    }
}