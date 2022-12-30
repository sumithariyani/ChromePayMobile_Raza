package com.example.chromepaymobile;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Api.Network.VolleyMultipartRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.scanlibrary.PickImageFragment;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.scanlibrary.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RegisterCustomer3Activity extends AppCompatActivity {

    MaterialButton submitBtnFrc3;
    ProgressBar progressBar;
    String mobile, assetType, assetId;
    Uri residence,document,registration;
    Bitmap bmp,proofResidenceImage,localGovDocumentImage,landRegistrationImage,residenceBmp,documentBmp,registrationBmp;
    LinearLayout proofResidence,logDocument,landRegistration;
    private final int GALLERY_REQ_CODE_RESIDENCE = 1100,GALLERY_REQ_CODE_DOCUMENT = 1200,GALLERY_REQ_CODE_REGISTRATION = 1300;
    byte[] byteArray,residenceArray,documentArray,registrationArray;
    EditText landSize;
    static Bitmap bitmap ;
    Bundle ex;
    ImageView proof,local,land,backImage;
    Spinner asset_type, asset_id, nationality;
    String msg;

    List<String> select_asset_type = new ArrayList<String>();
    List<String> select_asset_id = new ArrayList<String>();
    ArrayList<Bitmap> images = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer3);

        ex = getIntent().getExtras();
        bitmap = ex.getParcelable("image");
        System.out.println("register3Img "+ bitmap);

        submitBtnFrc3 = findViewById(R.id.submit_btn_frc3);
        progressBar = findViewById(R.id.progress_circular);
        landSize = findViewById(R.id.et_land_size);
        proofResidence = findViewById(R.id.proof_residence_l);
        logDocument = findViewById(R.id.local_gov_doc_l);
        landRegistration = findViewById(R.id.land_registration_l);
        backImage = findViewById(R.id.back_img_frc3);
        proof = findViewById(R.id.proof_residence_image);
        local = findViewById(R.id.local_gov_doc_image);
        land = findViewById(R.id.land_registration_image);
        asset_id = findViewById(R.id.sp_asset_id);
        asset_type = findViewById(R.id.sp_asset_type);

        mobile = getIntent().getStringExtra("mobile");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }


        select_asset_type.add("Select Asset");
        select_asset_type.add("Land");
        select_asset_type.add("Car");
        select_asset_type.add("House");
        select_asset_type.add("Store");


        select_asset_id.add("Select ID");
        select_asset_id.add("National ID");
        select_asset_id.add("Passport");
        select_asset_id.add("Driveing Licence");
        select_asset_id.add("Notarised Document");

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ArrayAdapter AssetTypeList = new ArrayAdapter(this,android.R.layout.simple_spinner_item,
                select_asset_type);

        AssetTypeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asset_type.setAdapter(AssetTypeList);

        asset_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                asset_type.setSelection(i);

                assetType = asset_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter AssetIdList = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                select_asset_id);
        AssetIdList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asset_id.setAdapter(AssetIdList);

        asset_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                asset_id.setSelection(i);
                assetId = asset_id.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        proofResidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int preference = ScanConstants.OPEN_CAMERA;
                Intent intent = new Intent(RegisterCustomer3Activity.this, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, GALLERY_REQ_CODE_RESIDENCE);

             /*   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               *//* intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Bitmap.CompressFormat.JPEG.toString();*//*
                startActivityForResult(intent,GALLERY_REQ_CODE_RESIDENCE);*/
            }
        });

        logDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int preference = ScanConstants.OPEN_CAMERA;
                Intent intent = new Intent(RegisterCustomer3Activity.this, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, GALLERY_REQ_CODE_DOCUMENT);

                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Bitmap.CompressFormat.JPEG.toString();
                startActivityForResult(intent,GALLERY_REQ_CODE_DOCUMENT);*/
            }
        });

        landRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int preference = ScanConstants.OPEN_CAMERA;
                Intent intent = new Intent(RegisterCustomer3Activity.this, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, GALLERY_REQ_CODE_REGISTRATION);
/*
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Bitmap.CompressFormat.JPEG.toString();
                startActivityForResult(intent,GALLERY_REQ_CODE_REGISTRATION);*/
            }
        });

        submitBtnFrc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (residenceBmp == null){
                    Toast.makeText(RegisterCustomer3Activity.this, "Please upload residence image", Toast.LENGTH_SHORT).show();
                }
                else if (localGovDocumentImage == null){
                    Toast.makeText(RegisterCustomer3Activity.this, "Please upload Gov_Doc", Toast.LENGTH_SHORT).show();
                }
                else if (registrationBmp == null){
                    Toast.makeText(RegisterCustomer3Activity.this, "Please upload land image", Toast.LENGTH_SHORT).show();
                }else{
                progressBar.setVisibility(View.VISIBLE);
                UpdateProductApi();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


/*
        if (resultCode == RESULT_OK){

            if (requestCode == GALLERY_REQ_CODE_RESIDENCE){

                residenceBmp = (Bitmap) (data.getExtras().get("data"));
                proof.setImageBitmap(residenceBmp);
            }
        }
*/

/*
        if (resultCode == RESULT_OK){

            if (requestCode == GALLERY_REQ_CODE_REGISTRATION){

                registrationBmp = (Bitmap) (data.getExtras().get("data"));
                land.setImageBitmap(registrationBmp);
            }
        }
*/


/*        if (resultCode == RESULT_OK){

            if (requestCode == GALLERY_REQ_CODE_DOCUMENT){

                documentBmp = (Bitmap) (data.getExtras().get("data"));
                local.setImageBitmap(documentBmp);
            }
        }*/

/*
        if (requestCode == GALLERY_REQ_CODE_RESIDENCE  && data != null && data.getData() != null && resultCode == Activity.RESULT_OK) {

            residenceBmp = (Bitmap) (data.getExtras().get("data"));
            proof.setImageBitmap((Bitmap) (data.getExtras().get("data")));

          */
/*  residence = data.getData();
            proof.setImageURI(data.getData());
            try {
                residenceBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), residence);
                Bitmap lastBitmap = null;
                lastBitmap = residenceBmp;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                lastBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                residenceArray = stream.toByteArray();
                proofResidenceImage = BitmapFactory.decodeByteArray(residenceArray, 0, residenceArray.length);

                String image = getStringImage(lastBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*//*

        }
*/


/*
        if (requestCode == GALLERY_REQ_CODE_DOCUMENT && data != null && data.getData() != null && resultCode == Activity.RESULT_OK) {

            documentBmp = (Bitmap) (data.getExtras().get("data"));
            local.setImageBitmap( (Bitmap) (data.getExtras().get("data")));
         */
/*   document = data.getData();
            local.setImageURI(data.getData());
            try {
                documentBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), document);
                Bitmap lastBitmap = null;
                lastBitmap = documentBmp;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                lastBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                documentArray = stream.toByteArray();
                localGovDocumentImage = BitmapFactory.decodeByteArray(documentArray, 0, documentArray.length);

                String image = getStringImage(lastBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*//*

        }
*/


/*
        if (requestCode == GALLERY_REQ_CODE_REGISTRATION && data != null && data.getData() != null && resultCode == Activity.RESULT_OK) {


          */
/*  registration = data.getData();
            land.setImageURI(data.getData());

            try {
                registrationBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), registration);
                Bitmap lastBitmap = null;
                lastBitmap = registrationBmp;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                lastBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                registrationArray = stream.toByteArray();
                landRegistrationImage = BitmapFactory.decodeByteArray(registrationArray, 0, registrationArray.length);

                String image = getStringImage(lastBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*//*

        }
*/

//////////////////////////////////////////////////////////////////////////////////////////
            if (requestCode == GALLERY_REQ_CODE_RESIDENCE && resultCode == Activity.RESULT_OK){

                residence = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);;
                Bitmap lastBitmap = null;

                try {
                    residenceBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), residence);
                    getContentResolver().delete(residence, null, null);
                    proof.setImageBitmap(residenceBmp);
                    Utils.getUri(this,residenceBmp);

                    lastBitmap = residenceBmp;

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    lastBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    residenceArray = stream.toByteArray();
                    proofResidenceImage = BitmapFactory.decodeByteArray(residenceArray, 0, residenceArray.length);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == GALLERY_REQ_CODE_DOCUMENT && resultCode == Activity.RESULT_OK) {

                document = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);

                Bitmap lastBitmap = null;

                try {
                    documentBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), document);
                    getContentResolver().delete(document, null, null);
                    local.setImageBitmap(documentBmp);
                    Utils.getUri(this,documentBmp);

                    lastBitmap = documentBmp;

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    lastBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    documentArray = stream.toByteArray();
                    localGovDocumentImage = BitmapFactory.decodeByteArray(documentArray, 0, documentArray.length);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            if (requestCode == GALLERY_REQ_CODE_REGISTRATION && resultCode == Activity.RESULT_OK) {

                registration = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap lastBitmap = null;

                try {
                    registrationBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), registration);
                    getContentResolver().delete(registration,null,null);
                    land.setImageBitmap(registrationBmp);
                    lastBitmap = registrationBmp;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    lastBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    registrationArray = stream.toByteArray();
                    landRegistrationImage = BitmapFactory.decodeByteArray(registrationArray, 0, registrationArray.length);

                    String image = getStringImage(lastBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public void Dialog() {

        final Dialog dialog = new Dialog(RegisterCustomer3Activity.this, android.R.style.Theme_Translucent_NoTitleBar);
        View mView = getLayoutInflater().inflate(R.layout.verification_dialodg_layout, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView dis = (ImageView) mView.findViewById(R.id.dis_dialog_loan);
        MaterialButton verify = (MaterialButton) mView.findViewById(R.id.verify_did_btn);
        EditText editText1 = (EditText) mView.findViewById(R.id.editText1);
        EditText editText2 = (EditText) mView.findViewById(R.id.editText2);
        EditText editText3 = (EditText) mView.findViewById(R.id.editText3);
        EditText editText4 = (EditText) mView.findViewById(R.id.editText4);
        EditText editText5 = (EditText) mView.findViewById(R.id.editText5);
        EditText editText6 = (EditText) mView.findViewById(R.id.editText6);
        ProgressBar progressBar1 = (ProgressBar) mView.findViewById(R.id.progress_verify);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView timer = (TextView) mView.findViewById(R.id.timer_txt);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout resend = (LinearLayout) mView.findViewById(R.id.resend_code_l);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView click_btn = (TextView) mView.findViewById(R.id.click_btn);

        dialog.setContentView(mView);

        progressBar.setVisibility(View.GONE);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Resend code after " + millisUntilFinished / 1000 + " sec");
                // logic to set the EditText could go here
            }

            public void onFinish() {
                timer.setText("Resend code after 00 sec");
                resend.setVisibility(View.VISIBLE);

            }

        }.start();

        {
            editText1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText2.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText3.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText4.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText5.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        editText6.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        {
            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText1.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            editText3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText2.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            editText4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText3.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText4.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            editText6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) {
                        editText5.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        click_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resend.setVisibility(View.GONE);

                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer.setText("Resend code after " + millisUntilFinished / 1000 + " sec");
                        // logic to set the EditText could go here
                    }

                    public void onFinish() {
                        timer.setText("Resend code after 00 sec");
                        resend.setVisibility(View.VISIBLE);
                    }

                }.start();
            }
        });

        click_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterCustomer3Activity.this);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.ResendOtp + mobile.substring(1, mobile.length()), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("RESEND_OTP ", response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                boolean status = jsonObject.getBoolean("status");
                                String msg = jsonObject.getString("msg");

                                if (status == true){

                                    Toast.makeText(RegisterCustomer3Activity.this, ""+msg, Toast.LENGTH_SHORT).show();


                                }else {
                                    Toast.makeText(RegisterCustomer3Activity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RegisterCustomer3Activity.this, ""+error, Toast.LENGTH_SHORT).show();
                                }
                            });

                    requestQueue.add(stringRequest);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar1.setVisibility(View.VISIBLE);

                {
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String otp = editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString()+editText5.getText().toString()+editText6.getText().toString();


                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("OTP",otp);
                        jsonBody.put("phoneNo",mobile.substring(1,mobile.length()));

                        System.out.println("jsonBody"+jsonBody);

                        final String mRequestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.VerifyCustomerAgent, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("responseOtp "+ response);
                                try {

                                    JSONObject jsonObject = new JSONObject(response);

                                    boolean status = jsonObject.getBoolean("status");
                                    String msg = jsonObject.getString("msg");

                                    if (status == true){
                                        Toast.makeText(RegisterCustomer3Activity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                        progressBar1.setVisibility(View.GONE);
                                      /*  final Dialog dialog1 = new Dialog(RegisterCustomer3Activity.this, android.R.style.Theme_Translucent_NoTitleBar);
                                        View mView = getLayoutInflater().inflate(R.layout.did_success_dialog_layout, null);
*/

                                        Intent intent = new Intent(RegisterCustomer3Activity.this, AgentDashActivity.class);
                                        startActivity(intent);
                                        finish();
/*
                                        MaterialButton ok = (MaterialButton) mView.findViewById(R.id.ok_btn);
*/
/*
                                        dialog1.setContentView(mView);
*/

                                        // Verify OTP Api////////////

//                                        ok.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                Intent intent = new Intent(RegisterCustomer3Activity.this, CustomerDashBoardActivity.class);
//                                                startActivity(intent);
//
//                                            }
//                                        });
//                                        dialog1.show();
                                    }else {
                                        Toast.makeText(RegisterCustomer3Activity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                        progressBar1.setVisibility(View.GONE);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("otp error " +error);
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

                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                5000,
                                1,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                        requestQueue.add(stringRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        dialog.show();

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public void UpdateProductApi(){

//        System.out.println("image3 "+getIntent().getExtras().getByteArray("image"));
//        byteArray = getIntent().getExtras().getByteArray("image");
//        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                AllUrl.Register2,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        Log.i("OTP_VOLLEY", new String(response.data));

                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data).toString());

                            boolean status = jsonObject.getBoolean("status");
                            msg = jsonObject.getString("msg");

                            if (jsonObject.has("service")) {
                                String service = jsonObject.getString("service");
                                if (status == false || service.matches("Linked")) {

                                    Toast.makeText(RegisterCustomer3Activity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                    LinkedDialog();

                                }
                            }else {
                                if (status == true) {


                                    Dialog();
                                    System.out.println(msg);
                                    Toast.makeText(RegisterCustomer3Activity.this, ""+msg, Toast.LENGTH_SHORT).show();

                                } if (status == false){
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterCustomer3Activity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError", "" + error.getMessage());
                        System.out.println("image_msg" +msg);
                        Toast.makeText(RegisterCustomer3Activity.this, ""+error, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();


                map.put("phone",mobile);
                map.put("assetType",assetType);
                map.put("assetID",assetId);
                map.put("email",getIntent().getStringExtra("email"));
                map.put("landSize",landSize.getText().toString().trim());
                System.out.println("UpdateProductApi parameter>>>>>>>> " + map);
                return map;
            }

            @SuppressLint("SuspiciousIndentation")
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                long imagename1 = System.currentTimeMillis();
                String simagename1 = Long.toString(imagename1);
                simagename1=simagename1.concat("simagename1");

                long imagename2 = System.currentTimeMillis();
                String simagename2 = Long.toString(imagename2);
                simagename2=simagename2.concat("simagename2");

                long imagename3 = System.currentTimeMillis();
                String simagename3 = Long.toString(imagename3);
                simagename3=simagename3.concat("simagename3");





                        params.put("residance", new DataPart(simagename1 + ".png", getFileDataFromDrawable(residenceBmp)));


                        params.put("locaDocument", new DataPart(simagename2 + ".png", getFileDataFromDrawable(documentBmp)));


                        params.put("landRegistration", new DataPart(simagename3 + ".png", getFileDataFromDrawable(registrationBmp)));

                System.out.println("UpdateProductApi product_image image>>>>1> " + params.toString());


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                9,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void LinkedDialog(){

        final Dialog linkDialog = new Dialog(RegisterCustomer3Activity.this, android.R.style.Theme_Translucent_NoTitleBar);
        View linkView = getLayoutInflater().inflate(R.layout.linked_service_dialog, null);


        MaterialButton button = (MaterialButton) linkView.findViewById(R.id.yes_btn_ls);
        TextView textView = (TextView) linkView.findViewById(R.id.cancel__btn);

        linkDialog.setContentView(linkView);

        progressBar.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterCustomer3Activity.this, LinkedServiceActivity.class);
                intent.putExtra("phone",getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkDialog.dismiss();
            }
        });
        linkDialog.show();
    }

}

