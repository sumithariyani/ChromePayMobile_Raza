package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

public class RegisterCustomer2Activity extends AppCompatActivity {

    MaterialButton next_btn_frc2;
    static Bitmap bitmap;
    Intent intent = getIntent();
    ImageView backImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer2);

       next_btn_frc2 = findViewById(R.id.next_btn_frc2);
       backImage = findViewById(R.id.back_img_frc2);

       backImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onBackPressed();
           }
       });

       String name = getIntent().getStringExtra("name");
       String mobile = getIntent().getStringExtra("mobile");
       String email = getIntent().getStringExtra("email");
       String address = getIntent().getStringExtra("address");
       String nationality = getIntent().getStringExtra("nationality");
       String profession = getIntent().getStringExtra("profession");
       String nickname = getIntent().getStringExtra("nickname");
       String phone = getIntent().getStringExtra("phone");

        System.out.println(mobile);

        System.out.println("dateOfBirth "+getIntent().getStringExtra("DOB"));

       next_btn_frc2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(RegisterCustomer2Activity.this,RegisterCustomer3Activity.class);
               intent.putExtra("name",getIntent().getStringExtra("name"));
               intent.putExtra("image", getIntent().getByteArrayExtra("image"));
               intent.putExtra("DOB",getIntent().getStringExtra("DOB"));
               intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
               intent.putExtra("email", getIntent().getStringExtra("email"));
               intent.putExtra("gender", getIntent().getStringExtra("gender"));
               intent.putExtra("nationality",getIntent().getStringExtra("nationality"));
               intent.putExtra("profession",getIntent().getStringExtra("profession"));
               intent.putExtra("nickname",getIntent().getStringExtra("nickname"));
               intent.putExtra("phone",getIntent().getStringExtra("phone"));
               intent.putExtra("address",getIntent().getStringExtra("address"));
               intent.putExtra("lat",getIntent().getStringExtra("lat"));
               intent.putExtra("lng",getIntent().getStringExtra("lng"));
               startActivity(intent);
           }
       });
    }
}