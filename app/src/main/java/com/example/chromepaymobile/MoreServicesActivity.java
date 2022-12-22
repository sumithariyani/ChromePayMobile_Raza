package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.chromepaymobile.Adapter.MSRecycleAdapter;
import com.example.chromepaymobile.Models.MSRecycleModel;

import java.util.ArrayList;

public class MoreServicesActivity extends AppCompatActivity {

    RecyclerView msRecycle;
    ImageView backImage;

    ArrayList<MSRecycleModel> msList = new ArrayList<>();
    MSRecycleAdapter msRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_services);

        msRecycle = findViewById(R.id.ms_recycle);
        backImage = findViewById(R.id.back_img_ms);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        msList.add(new MSRecycleModel("Micro Insurance",R.drawable.home_page_icon_10));
        msList.add(new MSRecycleModel("Micro  Pension",R.drawable.home_page_icon_12));
        msList.add(new MSRecycleModel("Remittance",R.drawable.home_page_icon_13));
        msList.add(new MSRecycleModel("Savings",R.drawable.home_page_icon_14));

        msRecycleAdapter = new MSRecycleAdapter(msList,getApplicationContext());
        msRecycle.setAdapter(msRecycleAdapter);
        msRecycleAdapter.notifyDataSetChanged();
    }
}