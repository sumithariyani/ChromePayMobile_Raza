package com.example.chromepaymobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class CustomerLoginActivity extends AppCompatActivity {

    ImageView backImgCl;
    EditText etCustomerId;
    MaterialButton submit;
    TextView registerCustomer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        backImgCl = findViewById(R.id.back_img_cl);
        etCustomerId = findViewById(R.id.et_customer_id);
        submit = findViewById(R.id.submit_btn);
        registerCustomer = findViewById(R.id.register_customer_tv);

        backImgCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etCustomerId.getText().toString().trim().isEmpty()){

                   Toast.makeText(CustomerLoginActivity.this, "Please Enter Id", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(CustomerLoginActivity.this, "Your id has bee submitted", Toast.LENGTH_SHORT).show();

                    final Dialog dialog = new Dialog(CustomerLoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                    View mView = getLayoutInflater().inflate(R.layout.verification_dialodg_layout, null);

                    ImageView dis = (ImageView) mView.findViewById(R.id.dis_dialog);
                    MaterialButton verify = (MaterialButton) mView.findViewById(R.id.verify_did_btn);
                    EditText editText1 = (EditText) mView.findViewById(R.id.editText1);
                    EditText editText2 = (EditText) mView.findViewById(R.id.editText2);
                    EditText editText3 = (EditText) mView.findViewById(R.id.editText3);
                    EditText editText4 = (EditText) mView.findViewById(R.id.editText4);
                    dialog.setContentView(mView);
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

                    }
                    dis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });

                    verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog1 = new Dialog(CustomerLoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                            View mView = getLayoutInflater().inflate(R.layout.did_success_dialog_layout, null);

                            MaterialButton ok = (MaterialButton) mView.findViewById(R.id.ok_btn);
                            dialog1.setContentView(mView);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(CustomerLoginActivity.this, CustomerDashBoardActivity.class);
                                    startActivity(intent);
                                }
                            });
                            dialog1.show();
                        }
                    });

                    dialog.show();
                }
            }
        });

        registerCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CustomerLoginActivity.this,RegisterCustomer1Activity.class);
                startActivity(intent);
            }
        });
    }
}