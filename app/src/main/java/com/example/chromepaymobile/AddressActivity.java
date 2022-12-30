package com.example.chromepaymobile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chromepaymobile.Api.Network.AllUrl;
import com.example.chromepaymobile.Api.Network.VolleyMultipartRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.button.MaterialButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddressActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    SearchView searchView;
    private GoogleMap mMap;
    TextView addresstv, loacality_tv,latitutetv,longitutetv;
    MaterialButton button;
    static Bitmap bitmap ;
    Bundle ex;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isLocationFinePermissionGranted = false;
    private boolean isLocationCoarsePermissionGranted = false;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        searchView = findViewById(R.id.search_view);
        addresstv = findViewById(R.id.address_tv);
        latitutetv = findViewById(R.id.latitute_tv);
        longitutetv = findViewById(R.id.longitute_tv);
        loacality_tv = findViewById(R.id.locality_address_tv);
        button = findViewById(R.id.countinue_btn_address);


        ex = getIntent().getExtras();
        bitmap = ex.getParcelable("image");

        System.out.println("addressImg "+ bitmap);
        System.out.println("mobile address "+getIntent().getStringExtra("nationality"));

        requestPermission();
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {


                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null){

                    isLocationFinePermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    Toast.makeText(AddressActivity.this, "Grante", Toast.LENGTH_SHORT).show();
                }

                if (result.get(Manifest.permission.ACCESS_COARSE_LOCATION) != null){

                    isLocationCoarsePermissionGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    Toast.makeText(AddressActivity.this, "Grante", Toast.LENGTH_SHORT).show();

                }
            }
        });



        try {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // on below line we are getting the
                    // location name from search view.
                    String location = searchView.getQuery().toString();

                    // below line is to create a list of address
                    // where we will store the list of all address.
                    List<Address> addressList = null;

                    try {

                        if (location != null || location.equals("")) {
                            // on below line we are creating and initializing a geo coder.
                            Geocoder geocoder = new Geocoder(AddressActivity.this);
                            try {
                                // on below line we are getting location from the
                                // location name and adding that location to address list.
                                addressList = geocoder.getFromLocationName(location, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // on below line we are getting the location
                            // from our list a first position.
                            Address address = addressList.get(0);

                            // on below line we are creating a variable for our location
                            // where we will add our locations latitude and longitude.
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            mMap.clear();
                            // on below line we are adding marker to that position.
                            mMap.addMarker(new MarkerOptions().position(latLng).title(address.getSubLocality()));
                            latitutetv.setText("Latitute: "+address.getLatitude());
                            longitutetv.setText("Longitute: "+address.getLongitude());
                            addresstv.setText("Address: "+address.getAddressLine(0));

                            System.out.println("SAddress: "+address.getAddressLine(1));
                            System.out.println("getSubAdminArea "+address.getSubAdminArea());
                            System.out.println("getAdminArea "+address.getAdminArea());

                            // below line is to animate camera to that position.
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // checking if the entered location is null or not.
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("dateOfBirth "+getIntent().getStringExtra("DOB"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterCustomer();

            }
        });
    }
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(AddressActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(addresses.get(0).getLocality());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.addMarker(markerOptions);

        latitutetv.setText("Latitute: "+currentLocation.getLatitude());
        longitutetv.setText("Longitute: "+currentLocation.getLongitude());
        addresstv.setText("Address: "+addresses.get(0).getAddressLine(0));

        System.out.println("Address: "+addresses);
        System.out.println("AddressLine: "+addresses.get(0).getAddressLine(0));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        if(mMap != null){
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    public void RegisterCustomer(){
        try {

            SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences",MODE_PRIVATE);
            String agentID = sharedPreferences.getString("ID","");
            String orgId = sharedPreferences.getString("orgID","");

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                    AllUrl.Register1+agentID+"/"+orgId,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            Log.i("OTP_VOLLEY", new String(response.data));

                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data).toString());

                                boolean status = jsonObject.getBoolean("status");
                                String msg = jsonObject.getString("msg");

                                    if (status == true) {

                                        System.out.println(msg);
                                        Intent intent = new Intent(AddressActivity.this, RegisterCustomer3Activity.class);

                                        intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                                        intent.putExtra("email", getIntent().getStringExtra("email"));
                                        startActivity(intent);


                                    } else {
//                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(AddressActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                    }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            progressBar.setVisibility(View.GONE);
                            Log.e("GotError", "" + error.getMessage());

                                Toast.makeText(AddressActivity.this, ""+error, Toast.LENGTH_SHORT).show();

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("fullname",getIntent().getStringExtra("name"));
                    map.put("dateOfBirth",getIntent().getStringExtra("DOB"));
                    map.put("phone",getIntent().getStringExtra("mobile"));
                    map.put("email",getIntent().getStringExtra("email"));
                    map.put("age",getIntent().getStringExtra("age"));
                    map.put("city",getIntent().getStringExtra("city"));
                    map.put("gender",getIntent().getStringExtra("gender"));
                    map.put("nationality",getIntent().getStringExtra("nationality"));
                    map.put("professoin",getIntent().getStringExtra("profession"));
                    map.put("nextFOKinName",getIntent().getStringExtra("nickname"));
                    map.put("nextFOKniPhone",getIntent().getStringExtra("phone"));
                    map.put("address",addresstv.getText().toString());
                    map.put("Latitude",latitutetv.getText().toString());
                    map.put("Longitude",longitutetv.getText().toString());
                    System.out.println("UpdateProductApi parameter>>>>>>>> " + map);
                    return map;
                }

                @SuppressLint("SuspiciousIndentation")
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                    long imagename0 = System.currentTimeMillis();
                    String simagename0 = Long.toString(imagename0);
                    simagename0=simagename0.concat("simagename0");

                    params.put("IDphoto", new DataPart(simagename0 + ".png", getFileDataFromDrawable(bitmap)));



                    System.out.println("UpdateProductApi product_image image>>>>1> " + params.toString());
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(volleyMultipartRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public boolean isLocationEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager != null && LocationManagerCompat.isLocationEnabled(manager);
    }

    private void requestPermission(){
/*
        Toast.makeText(this, "sadhfaljdhjhg", Toast.LENGTH_SHORT).show();
        isLocationFinePermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        isLocationCoarsePermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;


        List<String> permissionRequest = new ArrayList<String>();


        if (!isLocationFinePermissionGranted){

            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
            Toast.makeText(AddressActivity.this, "Not Grante", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "raza", Toast.LENGTH_SHORT).show();
        }

        if (!isLocationCoarsePermissionGranted){

            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            Toast.makeText(AddressActivity.this, "Not Grante", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "razakh", Toast.LENGTH_SHORT).show();
        }*/

        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage("permission Checked")
                    .setPositiveButton("proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
        }

       /* if (!permissionRequest.isEmpty()){

            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }*/
    }

}