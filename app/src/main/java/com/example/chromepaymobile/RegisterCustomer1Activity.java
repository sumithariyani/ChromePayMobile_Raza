package com.example.chromepaymobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterCustomer1Activity extends AppCompatActivity {

    EditText editText1,editText2,editText3,etName,etMobile,etEmail,etNationality,etProfession,etNickName,etPhone;
    MaterialButton next_btn_frc1;
    RadioGroup radioGroup;
    SimpleDateFormat simpleDateFormat;
    RadioButton male,female;
    CircleImageView uploadImg;
    ImageView backImage;
    Spinner countryCode, nationality;
    String str, CountryCode, mobile_number, NATIONALITY;
    static Bitmap bitmap ;
    Bundle ex;
    int year;
    int month;
    int day;

        private final int GALLERY_REQ_CODE = 1000;
    final Calendar calendar = Calendar.getInstance();

    List<String> select_code = new ArrayList<>();

    List<String> nationality_list = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer1);

        editText1 = findViewById(R.id.date_et);
        editText2 = findViewById(R.id.month_et);
        editText3 = findViewById(R.id.year_et);

        etName = findViewById(R.id.et_name_fcr1);
        etMobile = findViewById(R.id.et_mobile_fcr1);
        etEmail = findViewById(R.id.et_email_fcr1);
        etProfession = findViewById(R.id.et_profession_fcr1);
        etNickName = findViewById(R.id.et_nick_name_fcr1);
        etPhone = findViewById(R.id.et_phone_no_fcr1);
        next_btn_frc1=findViewById(R.id.next_btn_frc1);
        radioGroup = findViewById(R.id.radio);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        uploadImg = findViewById(R.id.profile_img_frc1);
        backImage = findViewById(R.id.back_img_frc1);
        countryCode = findViewById(R.id.country_code);
        nationality = findViewById(R.id.sp_nationality);


        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterCustomer1Activity.this,FaceRecognition.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        try {
                  ex = getIntent().getExtras();
        bitmap = ex.getParcelable("image");

        System.out.println("uploadImg "+ bitmap);
        uploadImg.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }


        select_code.add("+972");
        select_code.add("+251");
        select_code.add("+91");
        select_code.add("+1");

        nationality_list.add("Ethiopian");
        nationality_list.add("Indian");
        nationality_list.add("Israeli");
        nationality_list.add("American");

        ArrayAdapter CodeList = new ArrayAdapter(this, android.R.layout.simple_spinner_item, select_code);

        CodeList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCode.setAdapter(CodeList);

        countryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countryCode.setSelection(i);

                CountryCode = countryCode.getSelectedItem().toString();

                System.out.println("CountryCode "+CountryCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter Nationality = new ArrayAdapter(this, android.R.layout.simple_spinner_item, nationality_list);

        Nationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationality.setAdapter(Nationality);

        nationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nationality.setSelection(i);
                NATIONALITY = nationality.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    DatePickerDialog();
                }

                return true;
            }
        });


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        next_btn_frc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailToText = etEmail.getText().toString();
                String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                mobile_number = CountryCode + etMobile.getText().toString();
                System.out.println("mobile_number "+ mobile_number);

                if (bitmap == null){
                    Toast.makeText(RegisterCustomer1Activity.this, "Please upload your profile image", Toast.LENGTH_SHORT).show();
                }
                else if (editText1.getText().length()==0 || editText1.getText().length()==0 || editText3.getText().length()==0){
                    Toast.makeText(RegisterCustomer1Activity.this, "Please nter date of birth", Toast.LENGTH_SHORT).show();
                }
                else if (!emailToText.matches(emailPattern)){

                    Toast.makeText(RegisterCustomer1Activity.this, "Please Enter valid Email", Toast.LENGTH_SHORT).show();
                }else {

                    Intent intent = new Intent(RegisterCustomer1Activity.this, AddressActivity.class);
                    intent.putExtra("name", etName.getText().toString().trim());
                    intent.putExtra("image", bitmap);
                    intent.putExtra("DOB", editText1.getText().toString() + "-" + editText2.getText().toString() + "-" + editText3.getText().toString());
                    intent.putExtra("mobile", mobile_number);
                    intent.putExtra("email", etEmail.getText().toString());
                    intent.putExtra("gender", str);
                    intent.putExtra("nationality", NATIONALITY);
                    intent.putExtra("profession", etProfession.getText().toString());
                    intent.putExtra("nickname", etNickName.getText().toString());
                    intent.putExtra("phone", etPhone.getText().toString());
                    startActivity(intent);
                }
            }
        });

        DOBMove();
        DOBRemove();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (male.isChecked()){
                    str = "Male";
                }
                if (female.isChecked()){
                    str = "Female";
                }
                System.out.println(str);
            }
        });

    }
/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            if (requestCode == GALLERY_REQ_CODE && data != null && data.getData() != null){


                uploadImg.setImageURI(getIntent().);
                Uri uri = data.getData();
                System.out.println("URI "+uri);

                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    Bitmap lastBitmap = null;
                    lastBitmap = bitmap;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    lastBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);

                    byte[] byteArray = stream.toByteArray();
                    //encoding image to string
                    String image = getStringImage(lastBitmap);
                    System.out.println("byteArray"+byteArray);
                    System.out.println("lastBitmap"+lastBitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }
*/

/*    private String getFilename() {

        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }*/

    public void DOBMove(){

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length()==2){
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
                if (charSequence.toString().length()==2){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void DOBRemove(){

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().isEmpty()){
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

                if (charSequence.toString().isEmpty()){
                    editText1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void DatePickerDialog(){

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterCustomer1Activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                editText1.setText(String.valueOf(i2));
                editText2.setText(String.valueOf(i1+1));
                editText3.setText(String.valueOf(i));
            }
        },year,month,day);
        datePickerDialog.show();

    }

}
