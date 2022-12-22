package com.example.chromepaymobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LoanApplyCustomerDetaillActivity extends AppCompatActivity {

    PieChart pieChart;
    ArrayList<PieEntry> records = new ArrayList<>();
    TextView name,number,orgName,loanType,interest,EMI,totalAmount,durationYear,totalInterest;
    MaterialButton status;
    ImageView backImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_apply_customer_detaill);

        pieChart = findViewById(R.id.semi_pie_chart);
        name = findViewById(R.id.lac_name);
        number = findViewById(R.id.phone_lac);
        status = findViewById(R.id.pending_button);
        orgName = findViewById(R.id.org_name_tv);
        loanType = findViewById(R.id.loan_type_tv);
        interest = findViewById(R.id.interest_tv);
        EMI = findViewById(R.id.emi_tv);
        totalAmount = findViewById(R.id.total_amount_tv);
        durationYear = findViewById(R.id.duration_tv);
        totalInterest = findViewById(R.id.tia_tv);
        backImage = findViewById(R.id.back_img_lacd);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        name.setText(getIntent().getStringExtra("name"));
        number.setText(getIntent().getStringExtra("phone"));
        status.setText(getIntent().getStringExtra("status"));
        orgName.setText(getIntent().getStringExtra("oraganistaionName"));
        loanType.setText(getIntent().getStringExtra("loanType"));
        interest.setText(getIntent().getStringExtra("interest"));
        EMI.setText(getIntent().getStringExtra("EMI"));
        totalAmount.setText(getIntent().getStringExtra("totalAmount"));
        durationYear.setText(getIntent().getStringExtra("durationYear"));
        totalInterest.setText(getIntent().getStringExtra("totalInterestAmount"));


        records.add(new PieEntry(15,"Loans Paid"));
        records.add(new PieEntry(25,"Income"));
        records.add(new PieEntry(35,"Bill"));

        PieDataSet dataSet = new PieDataSet(records,"Pie Chart Report");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(22f);

        PieData pieData = new PieData(dataSet);
        pieChart.getDescription().setEnabled(false);
        pieChart.setData(pieData);
        pieChart.setCenterTextSize(20);
        pieChart.setMaxAngle(180);
        pieChart.getCenter();

    }
}