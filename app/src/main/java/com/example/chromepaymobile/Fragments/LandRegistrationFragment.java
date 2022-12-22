package com.example.chromepaymobile.Fragments;

import static com.example.chromepaymobile.R.id.land_certify_btn;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.io.DataInput;

import de.hdodenhof.circleimageview.CircleImageView;

public class LandRegistrationFragment extends Fragment {

    RelativeLayout landCertify;
    TextView fullName,phoneNO,addresses,landOwner,contact,landSize;
    CircleImageView profileImg;
    ImageView fingerPrintCUc;
    String proofofResidence,localGovDocument, landRegistration;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_land_registration, container, false);

        landCertify = mView.findViewById(R.id.land_certify_btn);
        fullName = mView.findViewById(R.id.fullNmae);
        phoneNO = mView.findViewById(R.id.phone_no_lr);
        addresses = mView.findViewById(R.id.address_cus);
        profileImg = mView.findViewById(R.id.image_profile_lr);
        fingerPrintCUc = mView.findViewById(R.id.finger_print_c_uc);
        landOwner = mView.findViewById(R.id.land_owner_cus);
        contact = mView.findViewById(R.id.contact_cus);
        landSize = mView.findViewById(R.id.land_size_cus);

        CUSTOMERDASHBOARD();

       /* Picasso.get().load(photo).placeholder(R.drawable.profile_01).fit().into(profileImg);
        fullName.setText(name);
        phoneNO.setText(number);
        address.setText(addresss);*/

     /*   if (fingerprint == 0){
            fingerPrintCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.cross));

        }else {
            fingerPrintCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.home_page_icon_22));

        }
*/
        Dialog();

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
                                    String address = object.getString("address");
                                    String nextFOKinName = object.getString("nextFOKinName");
                                    String nextFOKinPhone = object.getString("nextFOKniPhone");
                                    String walletAddress = object.getString("walletAddress");
                                    String LandSize = object.getString("landSize");
                                    int fingerPrint = object.getInt("fingerPrint");
                                    int facial = object.getInt("facialIdentification");
                                    int location = object.getInt("Location");
                                    proofofResidence = object.getString("residance");
                                    localGovDocument = object.getString("locaDocument");
                                    landRegistration = object.getString("landRegistration");

                                    fullName.setText(fullname);
                                    phoneNO.setText("+"+phoneNo);
                                    addresses.setText(address);
                                    landOwner.setText(fullname);
                                    contact.setText("+"+phoneNo);
                                    landSize.setText(LandSize);

                                    System.out.println(fingerPrint);

                                    System.out.println(IDphoto);
                                    Picasso.get()
                                            .load(IDphoto)
                                            .placeholder(R.drawable.image_background)
                                            .fit()
                                            .into(profileImg);

                                  /*  if (fingerPrint == 0){

                                        fingerPrintCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.cross));
                                    }else {
                                        fingerPrintCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.home_page_icon_22));
                                    }

                                    if (facial == 0){

                                        facialCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.cross));
                                    }else {
                                        facialCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.home_page_icon_22));

                                    }
*/
                                    if (location == 0){

                                        fingerPrintCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.cross));
                                    }else {
                                        fingerPrintCUc.setBackground(getActivity().getResources().getDrawable(R.drawable.home_page_icon_22));

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


    public void Dialog(){

        landCertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.land_certify_dialog,null);

                ImageView dis = (ImageView)dialogView.findViewById(R.id.land_dis);
                ImageView landImg = (ImageView)dialogView.findViewById(R.id.land_certify_img);

                dialog.setContentView(dialogView);
                Picasso.get()
                        .load(landRegistration)
                        .placeholder(R.drawable.progress_bar_iocn_07)
                        .fit()
                        .into(landImg);
                dis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

}