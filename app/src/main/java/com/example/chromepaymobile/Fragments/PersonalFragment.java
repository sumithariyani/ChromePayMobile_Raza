package com.example.chromepaymobile.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment {


    LinearLayout nationalBtn,proofResidenceBtn;
    CircleImageView imageProfile;
    ImageView fingerPrintCUc,facialCUc,locationCUc;
    TextView name,phone,dob,profession,genderTV,nickName,nickPhone,nationalityTV,addressTV,didRef;
    String proofofResidence,localGovDocument;
    int i =0;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_personal, container, false);

        nationalBtn = mView.findViewById(R.id.national_id_btn);
        proofResidenceBtn = mView.findViewById(R.id.proof_residence_btn);
        imageProfile = mView.findViewById(R.id.image_profile);
        name = mView.findViewById(R.id.name_tv_cus);
        didRef = mView.findViewById(R.id.did_ref);
        phone = mView.findViewById(R.id.phone_no);
        dob = mView.findViewById(R.id.dob_cus_tv);
        profession = mView.findViewById(R.id.et_profession_cus_tv);
        genderTV = mView.findViewById(R.id.gender_cus_tv);
        nickName = mView.findViewById(R.id.nickName_cus_tv);
        nickPhone = mView.findViewById(R.id.nickPhone_cus_tv);
        nationalityTV = mView.findViewById(R.id.nationality_cus_tv);
        addressTV = mView.findViewById(R.id.address_cus_tv);
        fingerPrintCUc = mView.findViewById(R.id.finger_print_c_uc);
        locationCUc = mView.findViewById(R.id.location_c_uc);
        facialCUc = mView.findViewById(R.id.facial_c_uc);

        CUSTOMERDASHBOARD();

        nationalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.national_id_dialog,null);

                ImageView dis = (ImageView) dialogView.findViewById(R.id.image_dis);
                ImageView nationalIdImage = (ImageView) dialogView.findViewById(R.id.national_id_img);

                dialog.setContentView(dialogView);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_tab_color));

                System.out.println(localGovDocument);
                Picasso.get().load(localGovDocument)
                        .placeholder(R.drawable.progress_bar_iocn_07)
                        .fit()
                        .into(nationalIdImage);

                dis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        proofResidenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getContext());
                View myDialogView = getLayoutInflater().inflate(R.layout.residence_proof_dialog,null);

                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView dis = (ImageView) myDialogView.findViewById(R.id.image_dis1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView proofresidenceimg = (ImageView) myDialogView.findViewById(R.id.proof_of_residence_image);

                dialog.setContentView(myDialogView);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_tab_color));

                System.out.println(proofofResidence);
                Picasso.get().load(proofofResidence)
                        .placeholder(R.drawable.progress_bar_iocn_07)
                        .fit()
                        .into(proofresidenceimg);
                dis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return mView;
    }

    public void CUSTOMERDASHBOARD(){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.CustomerDashBoard+getActivity().getIntent().getStringExtra("cust_id"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("CUSTOMERDASHBOARD.VOLLEY"+response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                boolean status = jsonObject.getBoolean("status");

                                if (status == true){

                                    JSONObject object = jsonObject.getJSONObject("obj");

                                    String IDphoto = object.getString("IDphoto");
                                    String fullname = object.getString("fullname");
                                    String dateOfBirth = object.getString("dateOfBirth");
                                    String didRefrence = object.getString("digitalrefID");
                                    String phoneNo = object.getString("phone");
                                    String gender = object.getString("gender");
                                    String nationality = object.getString("nationality");
                                    String professoin = object.getString("professoin");
                                    String address = object.getString("address");
                                    String nextFOKinName = object.getString("nextFOKinName");
                                    String nextFOKinPhone = object.getString("nextFOKniPhone");
                                    String walletAddress = object.getString("walletAddress");
                                    int fingerPrint = object.getInt("fingerPrint");
                                    int facial = object.getInt("facialIdentification");
                                    int location = object.getInt("Location");
                                    proofofResidence = object.getString("residance");
                                    localGovDocument = object.getString("locaDocument");
                                    String landRegistration = object.getString("landRegistration");

                                    name.setText(fullname);
                                    phone.setText("+"+ phoneNo);
                                    dob.setText(dateOfBirth);
                                    didRef.setText(didRefrence);
                                    nickPhone.setText(nextFOKinPhone);
                                    genderTV.setText(gender);
                                    nationalityTV.setText(nationality);
                                    profession.setText(professoin);
                                    addressTV.setText(address);
                                    nickName.setText(nextFOKinName);
                                    System.out.println(fingerPrint);

                                    System.out.println(IDphoto);
                                    Picasso.get()
                                            .load(IDphoto)
                                            .placeholder(R.drawable.image_background)
                                            .fit()
                                            .into(imageProfile);


                                    if (fingerPrint == 0){

                                        fingerPrintCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.cross));
                                    }else {
                                        fingerPrintCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.home_page_icon_22));
                                    }

                                    if (facial == 0){

                                        facialCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.cross));
                                    }else {
                                        facialCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.home_page_icon_22));

                                    }

                                    if (location == 0){

                                        locationCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.cross));
                                    }else {
                                        locationCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.home_page_icon_22));

                                    }

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