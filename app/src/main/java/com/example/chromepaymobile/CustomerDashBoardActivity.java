package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chromepaymobile.Adapter.CDBRecycleAdapter;
import com.example.chromepaymobile.Models.CDBRecycleModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CustomerDashBoardActivity extends AppCompatActivity {

    MaterialButton moreServicesBtn;
    RecyclerView cdbRecycle;
    ImageView backImage, logOut;

    ArrayList<CDBRecycleModel> cDBList = new ArrayList<>();
    CDBRecycleAdapter cdbRecycleAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash_board);

        moreServicesBtn = findViewById(R.id.more_services_btn);
        cdbRecycle = findViewById(R.id.cdb_recycle);
        backImage = findViewById(R.id.back_img_cdb);
        logOut = findViewById(R.id.log_out_btn);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("CustomerPreferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent3 = new Intent(CustomerDashBoardActivity.this,LoginActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent3);

                Toast.makeText(CustomerDashBoardActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("CustomerPreferences",MODE_PRIVATE);
        String ID = sharedPreferences.getString("custID","");


        cDBList.add(new CDBRecycleModel("Profile",R.drawable.home_page_icon_08));
        cDBList.add(new CDBRecycleModel("Transfers",R.drawable.home_page_icon_06));
        cDBList.add(new CDBRecycleModel("Microloans",R.drawable.home_page_icon_03));
        cDBList.add(new CDBRecycleModel("Bills",R.drawable.home_page_icon_04));
        cDBList.add(new CDBRecycleModel("Airtime",R.drawable.home_page_icon_05));
        cDBList.add(new CDBRecycleModel("Cash in / Cash out",R.drawable.home_page_icon_06));

        cdbRecycleAdapter = new CDBRecycleAdapter(cDBList, ID, CustomerDashBoardActivity.this);
        cdbRecycle.setAdapter(cdbRecycleAdapter);
        cdbRecycleAdapter.notifyDataSetChanged();

        moreServicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CustomerDashBoardActivity.this,MoreServicesActivity.class);
                startActivity(intent);
            }
        });
    }
}