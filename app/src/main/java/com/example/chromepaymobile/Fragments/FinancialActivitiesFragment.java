package com.example.chromepaymobile.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FinancialActivitiesFragment extends Fragment {

    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_financial_activities, container, false);

        pieChart = mView.findViewById(R.id.pie_chart);

        FinancialActivites();

//        pieChart.animate();
        return mView;
    }

    public void FinancialActivites(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.FinancialActivites+getActivity().getIntent().getStringExtra("cust_id"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("FINANCE_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");

                        JSONObject object = jsonObject.getJSONObject("obj1");

                        int reciving_amount = object.getInt("reciving_amount_per");
                        int bills_amount = object.getInt("bills_amount_per");
                        int recharge_amount = object.getInt("recharge_amount_per");
                        int loan_amount = object.getInt("Loan_amount_per");

                        ArrayList<PieEntry> records = new ArrayList<>();

                        records.add(new PieEntry(reciving_amount,"reciving_amount"));
                        records.add(new PieEntry(bills_amount, "bills_amount"));
                        records.add(new PieEntry(recharge_amount, "recharge_amount"));
                        records.add(new PieEntry(loan_amount, "Loan_amount"));

                        PieDataSet dataSet = new PieDataSet(records,"Pie Chart Report");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        dataSet.setValueTextColor(Color.BLACK);
                        dataSet.setValueTextSize(14f);

                        PieData pieData = new PieData(dataSet);

                        pieChart.setData(pieData);
                        /*pieChart.getDescription().setEnabled(true);*/
                        pieChart.setCenterText("Financial Activities");
                        pieChart.setCenterTextSize(18);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            requestQueue.add(stringRequest);
            
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}