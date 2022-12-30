package com.example.chromepaymobile;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SweepGradient;
import android.hardware.Camera;
import android.hardware.camera2.params.Face;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Api.Network.VolleyMultipartRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FaceRecognition extends AppCompatActivity {

    private final int CAMERA_REQ_CODE = 100;
    Bitmap img;
    TextView msg_txt;
    LinearLayout img_l;
    ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        img_l = findViewById(R.id.img_l);
        msg_txt = findViewById(R.id.please_txt);
        imageView = findViewById(R.id.scan_img);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQ_CODE);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
          super.onActivityResult(requestCode, resultCode, data);

          if (resultCode == RESULT_OK){

              if (requestCode == CAMERA_REQ_CODE){

                  img = (Bitmap) (data.getExtras().get("data"));

                  msg_txt.setVisibility(View.VISIBLE);
                  img_l.setVisibility(View.VISIBLE);
                  imageView.setVisibility(View.VISIBLE);

                  SendImage();

                  galleryAddPic();

                  Toast.makeText(this, "Your Image Start Scanning", Toast.LENGTH_LONG).show();

              }
          }else {
              onBackPressed();
          }

      }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(getStringImage(img));
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getApplicationContext().sendBroadcast(mediaScanIntent);
    }

    private void SendImage() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                AllUrl.FaceDetect,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        System.out.println("UpdateProductApi product>>>>>>>>> "+ new String(response.data) );

                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data).toString());

                            boolean status = jsonObject.getBoolean("status");
                            String msg = jsonObject.getString("msg");

                            if (status == true){
                                Toast.makeText(FaceRecognition.this, ""+ msg , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FaceRecognition.this,RegisterCustomer1Activity.class);
                                intent.putExtra("image",img);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }else {
                                Toast.makeText(FaceRecognition.this, ""+ msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FaceRecognition.this,RegisterCustomer1Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                onBackPressed();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GotError", "" + error.getMessage());
                        Toast.makeText(FaceRecognition.this, ""+error, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                return map;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();

                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(img)));

                System.out.println("UpdateProductApi product_image image>>>>1> " + params);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }


}