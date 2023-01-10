package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.chromepaymobile.Adapter.AgencyBankingAdaptre;
import com.example.chromepaymobile.Models.AgencyBankingModel;

import java.util.ArrayList;

public class AgencyBankingActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backImage;

    ArrayList<AgencyBankingModel> agencyBankingList = new ArrayList<AgencyBankingModel>();
    AgencyBankingAdaptre agencyBankingAdaptre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_banking);

        gridView = findViewById(R.id.agency_grid);
        backImage = findViewById(R.id.back_img_agency);

        agencyBankingList.add(new AgencyBankingModel("Transfers", R.drawable.home_page_icon_08));
        agencyBankingList.add(new AgencyBankingModel("Micro Loans", R.drawable.home_page_icon_06));
        agencyBankingList.add(new AgencyBankingModel("Micro Insurance", R.drawable.micro_insurance));
        agencyBankingList.add(new AgencyBankingModel("Micro Pensions", R.drawable.micro_loans));
        agencyBankingList.add(new AgencyBankingModel("Savings", R.drawable.saving));
        agencyBankingList.add(new AgencyBankingModel("Bills", R.drawable.home_page_icon_04));
        agencyBankingList.add(new AgencyBankingModel("Air Time", R.drawable.home_page_icon_05));
        agencyBankingList.add(new AgencyBankingModel("Cash In/Cash Out", R.drawable.home_page_icon_06));

        agencyBankingAdaptre = new AgencyBankingAdaptre(this,agencyBankingList);
        gridView.setAdapter(agencyBankingAdaptre);
        agencyBankingAdaptre.notifyDataSetChanged();
    }
}