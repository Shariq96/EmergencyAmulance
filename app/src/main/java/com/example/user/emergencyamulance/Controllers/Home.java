package com.example.user.emergencyamulance.Controllers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emergencyamulance.Helper.FragmentChangeListner;
import com.example.user.emergencyamulance.JSONParsing.GetDirectionData;
import com.example.user.emergencyamulance.JSONParsing.GetDriverMarkers;
import com.example.user.emergencyamulance.JSONParsing.JsonParsingObject;
import com.example.user.emergencyamulance.Models.GPSTracker;
import com.example.user.emergencyamulance.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;

import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, NavigationView.OnNavigationItemSelectedListener, FragmentChangeListner, android.location.LocationListener {

    public static final int REQUEST_LOCATION_CODE = 99;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    public static Button btn_req, btn_cancel;
    public static Boolean Status = false;
    public static String mobile_no, latLong, d_token, Trip_id, Click_action;
    public static FrameLayout f1;
    private static EditText editText;
    public boolean first_time = true;
    ArrayList driverList = new ArrayList();
    SupportMapFragment mapFragment;
    GPSTracker gpsTracker;
    String hello;
    String url = "http://192.168.0.101:51967/api/useracc/GetRequest";
    String token = FirebaseInstanceId.getInstance().getToken();
    SpotsDialog _progdialog;
    CancelTrip cf = new CancelTrip();
    JsonParsingObject jb = new JsonParsingObject();
    JSONObject jbobj;
    int destination_reqcode = 1;
    int sourcePicker_req = 2;
    boolean isFirst_time = true;
    OkHttpClient Client = new OkHttpClient();
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest request;
    private Location lastLocation;
    private PlaceAutocomplete places;
    private Location myloc;
    private Marker currentLocation;
    private LocationManager locationManager;
    private double destlang, destlat;
    private TextView estdistance;
    private TextView estfare;
    private AutocompleteFilter _typeFilter;
    private LatLngBounds bounds;
    private String service;
    //place api intent builder
    private PlacePicker.IntentBuilder destinationLoc_Builder;
    private PlacePicker.IntentBuilder sourceLoc_Builder;
    private double sourceLongitude;
    private double sourceLatitude;
    private Location location;
    private EditText destinationAddr;
    private EditText sourceAddress;
    private AutoCompleteTextView _autosearchaddr;
    private double sourclan;
    private double sourclon;
    private LatLng sourcelocation;
    private int baseFee = 300;
    private int costPerKM = 10;
    private String sourceAddressName;
    private LatLng gpsTrackerLocation;
    private String destAddressName;
    private boolean locationFlag = false;
    private TextView textView;
    SharedPreferences MyPref;
    SharedPreferences.Editor editor;
    private BottomNavigationView bottomBar;
    private String serviceType = ""; // 1 for Routine, 2 for Crictical, 3 for Normal
    private LatLng finalsourceLocation;
    private ImageView myLocationTrackerIcon;
    private BroadcastReceiver mMsgReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Status = true;
            displayAlert(intent);
        }
    };
    private BroadcastReceiver displayDrivers = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            driverList = intent.getStringArrayListExtra("list");
            showDriver(driverList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_home);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);



        //TODO: En k tafseel likhu idr Comment kar k
        // bhai yeh navigaton drawer ka builtin h
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MyPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = MyPref.edit();
        String new1 = MyPref.getString("name","user").toString();
        textView = (TextView) findViewById(R.id.lbl_sidenav_username);
       // textView.setText();


        gpsTrackerLocation = getMyLocation();

        //initializing items
        sourceAddress = (EditText) findViewById(R.id._source);
        estdistance = (TextView) findViewById(R.id.txt_Distance);
        estfare = (TextView) findViewById(R.id.txt_Fare);
        //  btn_cancel = (Button) findViewById(R.id.btn_cncel);
        destinationAddr = (EditText) findViewById(R.id._destination);
        btn_req = (Button) findViewById(R.id.btn_req);
        //   _autosearchaddr = (AutoCompleteTextView) findViewById(R.id._autosource);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomNavigationBar);
        myLocationTrackerIcon = (ImageView) findViewById(R.id.trackmylocation);


        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navbar_routine:
                        serviceType = "1";
                        break;
                    case R.id.navbar_crictical:
                        serviceType = "2";
                        break;
                    case R.id.navbar_deadbody:
                        serviceType = "3";
                        break;
                }
                return true;
            }
        });


        //Service for Drivers Location
        startService(new Intent(this, GetDriverMarkers.class));
        //EnablingLocationServices
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        myLocationTrackerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsTracker.canGetLocation()) {
                    LatLng currentLocation = getMyLocation();
                    setSourceLocationAddress(currentLocation);
                    setMyLocationMarker(mMap, currentLocation);
                }
            }
        });


        //Updated 28Feb2018 - getMyLocationPointer
        if (mMap != null && mapFragment.getView().findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }

        //TODO: Ye kia scene hai bae comment to kr
        // getting map fragment and myFunction  screen per dikhata h k apka driver n req accept ki h
        // dd jo driver ki location arahi h wo map per show karta h
        mapFragment.getMapAsync(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMsgReciver,
                new IntentFilter("myFunction"));
        LocalBroadcastManager.getInstance(this).registerReceiver(displayDrivers,
                new IntentFilter("dd"));


        if (locationFlag == true) {
            finalsourceLocation = new LatLng(sourcelocation.latitude, sourcelocation.longitude);
        } else {
//            finalsourceLocation = new LatLng(gpsTrackerLocation.latitude,gpsTrackerLocation.longitude);
        }

        //TODO: RequestButton
        btn_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
                    jbobj = jb.reqObject("03338983584",finalsourceLocation.latitude,finalsourceLocation.longitude,token,serviceType);
//                    _progdialog.show();
                    run(url, v,jbobj);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
                Intent intent = new Intent(getApplicationContext(), FeedbackController.class);
                startActivity(intent);
                finish();
            }
        });


        //Places Api OnClickListeners
        destinationAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationLoc_Builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = destinationLoc_Builder.build(Home.this);
                    startActivityForResult(intent, destination_reqcode);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        sourceAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceLoc_Builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = sourceLoc_Builder.build(Home.this);
                    startActivityForResult(intent, sourcePicker_req);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
//       textView.setText(hello);
    }

    //getting current Location LatLng for mapReady and tracker button
    private LatLng getMyLocation() {

        //Getting Location Co-ordinates
        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {

            sourceLatitude = gpsTracker.getLatitude();
            sourceLongitude = gpsTracker.getLongitude();
            gpsTrackerLocation = new LatLng(sourceLatitude, sourceLongitude);
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + sourceLatitude + "\nLong: " + sourceLongitude, Toast.LENGTH_LONG).show();
            //setting default location address oncreate

        } else {
            gpsTracker.showSettingsAlert();
        }

        return gpsTrackerLocation;
    }

    private void setSourceLocationAddress(LatLng currentLatLng) {

        String sourceAddr = getCompleteAddressString(currentLatLng.latitude, currentLatLng.longitude);
        sourceAddress.setText(sourceAddr);

    }

    private void setMyLocationMarker(GoogleMap googleMap, LatLng latLng) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        MarkerOptions markerOptions = new MarkerOptions()
                .title(sourceAddressName)
                .position(latLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocationtrackicon));
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 15));

    }

    //PlacesApiResults and Implementation
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == destination_reqcode) {
            if (resultCode == RESULT_OK) {
                boolean isAnHospital = false;
                Place place = PlacePicker.getPlace(data, this);
                for (int i : place.getPlaceTypes()) {
                    if (i == Place.TYPE_DENTIST || i == Place.TYPE_DOCTOR || i == Place.TYPE_HOSPITAL || i == Place.TYPE_HEALTH) {
                        isAnHospital = true;
                        break;
                    }
                }

                if (isAnHospital == true) {
                    destAddressName = String.format(" %s", place.getName());
                    LatLng destinationlocation = place.getLatLng();
                    destlat = destinationlocation.latitude;
                    destlang = destinationlocation.longitude;
                    String destaddr_Name = getCompleteAddressString(destlat, destlang);
                    destinationAddr.setText(destaddr_Name);
                    setDestMarker(destlang, destlat);


                } else {
                    Toast.makeText(this, "Please Select Appropriate Location", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == sourcePicker_req) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                sourceAddressName = String.format(" %s", place.getName());

                sourcelocation = place.getLatLng();
                String finaladdr = getCompleteAddressString(sourcelocation.latitude, sourcelocation.longitude);
                sourceAddress.setText(finaladdr);
                locationFlag = true;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(sourcelocation);
                markerOptions.title(sourceAddressName);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocationtrackicon));
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.zoomBy(-2));
            }
        } else {
            Toast.makeText(this, "Please Select Again", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction addr", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction addr", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction addr", "Canont get Address!");
        }
        return strAdd;
    }

    private void setDestMarker(double destination_lang, double destination_lat) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(destination_lat, destination_lang));
        markerOptions.title(destAddressName);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        float result[] = new float[10];
        //TODO: Es ko check karo kia panga de ra
        if (sourceLatitude >= 0.0) {
            Location.distanceBetween(gpsTrackerLocation.latitude, gpsTrackerLocation.longitude, destination_lat, destination_lang, result);
        } else {
            Location.distanceBetween(sourcelocation.latitude, sourcelocation.longitude, destination_lat, destination_lang, result);
        }

        int distance = (int) result[0] / 1000;

        estdistance.setText("Distance: " + distance + "km");
        estfare.setText("Est. Fare: " + ((distance * costPerKM) + baseFee) + "Rs");

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.destinationmarkericon));
        markerOptions.snippet("Distance = " + result[0]);
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(8));
        setDirections();


    }

    private void setDirections() {
        Object[] dataTransfer = new Object[3];
        String url = getDirectionUrl();
        GetDirectionData gdd = new GetDirectionData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = new LatLng(destlat, destlang);
        gdd.execute(dataTransfer);

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMsgReciver);
        super.onDestroy();
    }

    private void showDriver(ArrayList driverList) {
        mMap.clear();
        for (int i = 0; i < driverList.size(); i++) {

            String temp = driverList.get(i).toString();
            String[] latlong = temp.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(latitude, longitude));
            mMap.addMarker(markerOptions);

        }


    }

    private void displayAlert(Intent intent) {
        mobile_no = intent.getStringExtra("title");
        latLong = intent.getStringExtra("body");
        d_token = intent.getStringExtra("drivertoken_no");
        Click_action = intent.getStringExtra("clickaction");
        Trip_id = intent.getStringExtra("Trip_id");

        if (Click_action == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(mobile_no)
                    .setCancelable(
                            true)
                    .setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            btn_cancel.setVisibility(View.GONE);
            btn_req.setVisibility(View.VISIBLE);
            Toast.makeText(this, "REASSINING DRIVER. Please Wait", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_policies) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_howitworks) {

        } else if (id == R.id.nav_signout) {
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            FirebaseAuth.getInstance().signOut();
            editor.putBoolean("login", false);
            editor.apply();
            startActivity(new Intent(Home.this, LoginController.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);

            }
            return false;
        } else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (isFirst_time) {
            isFirst_time = false;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        MarkerOptions options = new MarkerOptions();
        mMap = googleMap;

        setSourceLocationAddress(gpsTrackerLocation);
        setMyLocationMarker(mMap, gpsTrackerLocation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleApiClient();
                mMap.setMyLocationEnabled(false);
                mMap.setTrafficEnabled(false);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(true);


            }
        } else {
            googleApiClient();
            mMap.setMyLocationEnabled(false);
            mMap.setTrafficEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);

        }
    }

   /* public void CalcDistance(LatLng[] ltlong) {
        String result = null;
        float temp = 0;
        Location loc1 = new Location("");
        loc1.setLatitude(myloc.getLatitude());
        loc1.setLongitude(myloc.getLongitude());
        for (int i = 0; i != ltlong.length; i++) {
            Location loc2 = new Location("");
            loc2.setLatitude(ltlong[i].sourceLatitude);
            loc2.setLongitude(ltlong[i].longitude);
            float distanceInMeters = loc1.distanceTo(loc2);
            if (i == 0) {
                temp = distanceInMeters;

            } else {
                if (temp > distanceInMeters) {
                    result = "ambulance" + i + "is near";
                }
            }
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    } */

    protected synchronized void googleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        client.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission is granted
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            googleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "PermissionDenied", Toast.LENGTH_LONG).show();
                }
        }
    }

    public void run(String url, View v, JSONObject jbobj) throws IOException {
        // OkHttpClient client = new OkHttpClient();
        //  LatLng latLng = new LatLng(sourceLatitude, sourceLongitude);
        RequestBody body = RequestBody.create(JSON, jbobj.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "somethng went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String myResponse = response.body().string();
                //  myResponse = myResponse.substring(1, myResponse.length() - 1); // yara
                myResponse = myResponse.replace("\\", "");
                hello = myResponse;
                //JSONObject jarray = null;
                //  jarray = new JSONObject(myResponse);
                //api_pass = jarray.getString("password");
                // api_pass = myResponse;
                //api_pass = jarray.getJSONObject(0).getString("password");

                Home.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hello.equals("true")) {
                            Toast.makeText(getApplicationContext(), "Got it", Toast.LENGTH_LONG).show();
                            btn_req.setVisibility(View.GONE);
                            btn_cancel.setVisibility(View.VISIBLE);
//                            _progdialog.cancel();

                        } else {
                            Toast.makeText(getApplicationContext(), "No Amb Nearby", Toast.LENGTH_LONG).show();
                        }
                    }

                });


            }
        });

    }

    private String getDirectionUrl() {
        StringBuilder gDirectionUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        gDirectionUrl.append("origin=" + finalsourceLocation.latitude + "," + finalsourceLocation.longitude);
        gDirectionUrl.append("&destination=" + destlat + "," + destlang);
        gDirectionUrl.append("&key=" + "AIzaSyB5WDX6S95k_KvAdN7PjdXzz9XIneDhIsc");
        return (gDirectionUrl.toString());
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // fragmentTransaction.replace(R.id.frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}
