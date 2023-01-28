package com.example.chromepaymobile;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.scanlibrary.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class AgentPerformenceActivity extends AppCompatActivity {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    int  dec,jan, feb, mar, apr, may, june, july, aug, sep, oct, nov, december;
    int year1,year2, year3, year4, year5, year6, year7, year8, year9;
    int friday,saturday, sunday, monday, tuesday, wednesday, thursday;
    TextView name, userNumber, agEmail, location, transaction;
    ImageView backImage, imageProfile;
    Spinner spinner;
    String filter;

    ArrayList barEntriesArrayList = new ArrayList<>();
    List<String> select_filter = new ArrayList<String>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.chromepaymobile.R.layout.activity_agent_performence);

        barChart = findViewById(com.example.chromepaymobile.R.id.bar_chart);
        name = findViewById(com.example.chromepaymobile.R.id.agent_name_tv);
        agEmail = findViewById(com.example.chromepaymobile.R.id.email_ap);
        location = findViewById(com.example.chromepaymobile.R.id.agent_location_tv);
        userNumber = findViewById(com.example.chromepaymobile.R.id.up_status_text);
        transaction = findViewById(com.example.chromepaymobile.R.id.down_status_text);
        backImage = findViewById(com.example.chromepaymobile.R.id.back_img_ap);
        imageProfile = findViewById(com.example.chromepaymobile.R.id.image_profile_all_did);
        spinner = findViewById(com.example.chromepaymobile.R.id.sp_filter);


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        select_filter.add("Year");
        select_filter.add("Month");
        select_filter.add("Day");

        ArrayAdapter FilterList = new ArrayAdapter(this,android.R.layout.simple_spinner_item,
                select_filter);

        FilterList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(FilterList);

        String selecton = "Month";
        int spinnerPosition = FilterList.getPosition(selecton);
        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                filter = spinner.getSelectedItem().toString();
                System.out.println(filter);
                getDataEntries();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AgentDashApi();

    }


    public void getDataEntries(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("filter",filter);

            System.out.println("jsonBody"+jsonBody);

            final String mRequestBody = jsonBody.toString();

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String token = sharedPreferences.getString("token","");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.AgentPerformanceReport+token, new Response.Listener<String>() {
                @SuppressLint("SuspiciousIndentation")
                @Override
                public void onResponse(String response) {
                    Log.i("PERFORMANCE_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);


                            if (jsonObject.has("Year")) {

                                JSONObject object = jsonObject.getJSONObject("Year");

                                year1 = object.getInt("2018");
                                year2 = object.getInt("2019");
                                year3 = object.getInt("2020");
                                year4 = object.getInt("2021");
                                year5 = object.getInt("2022");
                                year6 = object.getInt("2023");
                                year7 = object.getInt("2024");
                                year8 = object.getInt("2025");
                                year9 = object.getInt("2026");


                                ArrayList<String> xAxisLabel = new ArrayList<>();

                                xAxisLabel.add("2018");
                                xAxisLabel.add("2019");
                                xAxisLabel.add("2020");
                                xAxisLabel.add("2021");
                                xAxisLabel.add("2022");
                                xAxisLabel.add("2023");
                                xAxisLabel.add("2024");
                                xAxisLabel.add("2025");

                                XAxis xAxis = barChart.getXAxis();
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                                xAxis.setGranularityEnabled(true);

                                barEntriesArrayList.add(new BarEntry(0, year1));
                                barEntriesArrayList.add(new BarEntry(1, year2));
                                barEntriesArrayList.add(new BarEntry(2, year3));
                                barEntriesArrayList.add(new BarEntry(3, year4));
                                barEntriesArrayList.add(new BarEntry(4, year5));
                                barEntriesArrayList.add(new BarEntry(5, year6));
                                barEntriesArrayList.add(new BarEntry(6, year7));
                                barEntriesArrayList.add(new BarEntry(7, year8));
                                barEntriesArrayList.add(new BarEntry(8, year9));


                                barDataSet = new BarDataSet(barEntriesArrayList, "");
                                barData = new BarData(barDataSet);

                                barChart.setData(barData);
                                barDataSet.setColors(getResources().getColor(com.example.chromepaymobile.R.color.teal_700));
                                barDataSet.setValueTextColor(Color.BLACK);
                                barDataSet.setValueTextSize(10f);
                                barChart.getDescription().setEnabled(false);

                                barChart.setTouchEnabled(true);
                                barChart.getXAxis().setDrawGridLines(false);


                                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                barChart.notifyDataSetChanged();
                                barChart.invalidate();
                            }

                        if (jsonObject.has("Month")) {

                            JSONObject object = jsonObject.getJSONObject("Month");
                            dec = object.getInt("January");
                            jan = object.getInt("February");
                            feb = object.getInt("March");
                            mar = object.getInt("April");
                            apr = object.getInt("May");
                            may = object.getInt("June");
                            june = object.getInt("July");
                            july = object.getInt("August");
                            aug = object.getInt("September");
                            sep = object.getInt("October");
                            oct = object.getInt("November");
                            nov = object.getInt("December");


                            ArrayList<String> xAxisLabel = new ArrayList<>();

                            xAxisLabel.add("Dec");
                            xAxisLabel.add("Jan");
                            xAxisLabel.add("Feb");
                            xAxisLabel.add("Mar");
                            xAxisLabel.add("Apr");
                            xAxisLabel.add("May");
                            xAxisLabel.add("June");
                            xAxisLabel.add("July");
                            xAxisLabel.add("Aug");
                            xAxisLabel.add("Sep");
                            xAxisLabel.add("Oct");
                            xAxisLabel.add("Nov");
                            xAxisLabel.add("Dec");

                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                            xAxis.setGranularityEnabled(true);

                            barEntriesArrayList.add(new BarEntry(0, dec));
                            barEntriesArrayList.add(new BarEntry(1, jan));
                            barEntriesArrayList.add(new BarEntry(2, feb));
                            barEntriesArrayList.add(new BarEntry(3, mar));
                            barEntriesArrayList.add(new BarEntry(4, apr));
                            barEntriesArrayList.add(new BarEntry(5, may));
                            barEntriesArrayList.add(new BarEntry(6, june));
                            barEntriesArrayList.add(new BarEntry(7, july));
                            barEntriesArrayList.add(new BarEntry(8, aug));
                            barEntriesArrayList.add(new BarEntry(9, sep));
                            barEntriesArrayList.add(new BarEntry(10, oct));
                            barEntriesArrayList.add(new BarEntry(11, nov));
                            barEntriesArrayList.add(new BarEntry(12, december));

                            barDataSet = new BarDataSet(barEntriesArrayList, "");
                            barData = new BarData(barDataSet);

                            barChart.setData(barData);
                            barDataSet.setColors(getResources().getColor(com.example.chromepaymobile.R.color.teal_700));
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(10f);
                            barChart.getDescription().setEnabled(false);

                            barChart.setTouchEnabled(true);
                            barChart.getXAxis().setDrawGridLines(false);


                            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChart.notifyDataSetChanged();
                            barChart.invalidate();
                        }

                        if (jsonObject.has("Day")) {

                            JSONObject object = jsonObject.getJSONObject("Day");
                            monday = object.getInt("Monday");
                            tuesday = object.getInt("Tuseday");
                            wednesday = object.getInt("Wednesday");
                            thursday = object.getInt("Thrusday");
                            friday = object.getInt("Friday");
                            saturday = object.getInt("Saturday");
                            sunday = object.getInt("Sunday");


                            ArrayList<String> xAxisLabel = new ArrayList<>();

                            xAxisLabel.add("Mon");
                            xAxisLabel.add("Tue");
                            xAxisLabel.add("Wednes");
                            xAxisLabel.add("Thu");
                            xAxisLabel.add("Fri");
                            xAxisLabel.add("Sat");
                            xAxisLabel.add("Sun");


                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                            xAxis.setGranularityEnabled(true);

                            barEntriesArrayList.add(new BarEntry(0, monday));
                            barEntriesArrayList.add(new BarEntry(1, tuesday));
                            barEntriesArrayList.add(new BarEntry(2, wednesday));
                            barEntriesArrayList.add(new BarEntry(3, thursday));
                            barEntriesArrayList.add(new BarEntry(4, friday));
                            barEntriesArrayList.add(new BarEntry(5, saturday));
                            barEntriesArrayList.add(new BarEntry(6, sunday));

                            barDataSet = new BarDataSet(barEntriesArrayList, "");
                            barData = new BarData(barDataSet);

                            barChart.setData(barData);
                            barDataSet.setColors(getResources().getColor(com.example.chromepaymobile.R.color.teal_700));
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(10f);
                            barChart.getDescription().setEnabled(false);

                            barChart.setTouchEnabled(true);
                            barChart.getXAxis().setDrawGridLines(false);


                            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            barChart.notifyDataSetChanged();
                            barChart.invalidate();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AgentPerformenceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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

    public void AgentDashApi(){

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String agentId = sharedPreferences.getString("ID","");

            StringRequest stringRequest = new StringRequest(Request.Method.GET, AllUrl.AgentDashInfo+agentId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("AGENT_DASH_VOLLEY", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");
                        String NumberOFUser = jsonObject.getString("NumberOFUser");
                        String image = jsonObject.getString("image");
                        String agentName = jsonObject.getString("agentName");
                        String email = jsonObject.getString("email");
                        String country = jsonObject.getString("country");
                        int mobile = jsonObject.getInt("mobile");
                        String totalTransection = jsonObject.getString("totalTransection");

                        if (status == true) {
                            name.setText(agentName);
                            location.setText(country);
                            agEmail.setText(email);
                            userNumber.setText(NumberOFUser);
                            transaction.setText(totalTransection);
                            Picasso.get()
                                    .load(image)
                                    .placeholder(com.example.chromepaymobile.R.drawable.all_dids_06)
                                    .fit()
                                    .into(imageProfile);
                        }
                    } catch (JSONException e) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}