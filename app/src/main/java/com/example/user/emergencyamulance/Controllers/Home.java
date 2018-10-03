package com.example.user.emergencyamulance.Controllers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.support.v7.widget.CardView;
import android.text.Layout;
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
import android.widget.LinearLayout;
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
import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
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
    String mintime;
    public static String urlwhole = "http://192.168.8.102:51967/api/";
    private FirebaseAuth mAuth;
    SupportMapFragment mapFragment;
    GPSTracker gpsTracker;
    String hello;
    View v1;
    String url = urlwhole + "useracc/GetRequest";
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
    LinearLayout liner;
    private TextView estfare;
    private AutocompleteFilter _typeFilter;
    private LatLngBounds bounds;
    private String service;
    //place api intent builder
    private PlacePicker.IntentBuilder destinationLoc_Builder;
    private PlacePicker.IntentBuilder sourceLoc_Builder;
    private double sourceLongitude;
    private double sourceLatitude;
    public static double sourceLongitude1;
    public static double sourceLatitude1;
    private Location location;
    private EditText destinationAddr;
    private TextView nearestAmbtext;
    private EditText sourceAddress;
    private AutoCompleteTextView _autosearchaddr;
    private double sourclan;
    private double sourclon;
    private TextView lbl_sidenav_userName;
    private LatLng sourcelocation;
    Button cancel;
    private TextView estdistance, Trip_status;
    private int baseFee = 300;
    private int costPerKM = 10;
    private String sourceAddressName;
    private LatLng gpsTrackerLocation;
    private String destAddressName;
    private ArcNavigationView NavigationViewArc;
    private boolean locationFlag = false;
    private TextView textView;
    int skipindex = 0;
    SharedPreferences MyPref;
    SharedPreferences.Editor editor;
    private boolean tripflag = false;
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
    private BroadcastReceiver displayMintime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mintime = intent.getStringExtra("minTime");
            showmin(mintime);
        }
    };


    private CardView trip_panel;
    private CardView request_panel;
    private Button contact;

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


        cancel = (Button) findViewById(R.id.cancel_ride);
        contact = (Button) findViewById(R.id.contact_id);
        liner = (LinearLayout) findViewById(R.id.idla);
        Trip_status = (TextView) findViewById(R.id.statustrip);
        f1 = (FrameLayout) findViewById(R.id.frame);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_phone_black_24dp);
        toolbar.setOverflowIcon(drawable);

        NavigationView navigattionView = findViewById(R.id.nav_view);
        navigattionView.setNavigationItemSelectedListener(this);


        MyPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = MyPref.edit();
        String new1 = MyPref.getString("name","user").toString();
        View hView = navigattionView.getHeaderView(0);
        textView = (TextView) hView.findViewById(R.id.username);
        textView.setText(new1);


        gpsTrackerLocation = getMyLocation();

        //initializing items
        sourceAddress = (EditText) findViewById(R.id._source);
        estdistance = (TextView) findViewById(R.id.txt_Distance);
        estfare = (TextView) findViewById(R.id.txt_Fare);
        nearestAmbtext = (TextView) findViewById(R.id._nearestambulancetext);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        destinationAddr = (EditText) findViewById(R.id._destination);
        btn_req = (Button) findViewById(R.id.btn_req);
        //   _autosearchaddr = (AutoCompleteTextView) findViewById(R.id._autosource);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomNavigationBar);
        myLocationTrackerIcon = (ImageView) findViewById(R.id.trackmylocation);
        trip_panel = findViewById(R.id.trip_panel);
        request_panel= findViewById(R.id.request_panel);

          /*  btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        btn_req.setVisibility(View.VISIBLE);
                        btn_cancel.setVisibility(View.GONE);
                }
            });*/

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

     /*   contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:" + mobile_no));    //this is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(Home.this,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                } else {     //have got permission
                    try {
                        startActivity(callIntent);  //call activity and make phone call
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Home.this, "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
        //Service for Drivers Location
        startService(new Intent(this, GetDriverMarkers.class));
        //EnablingLocationServices
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1.setVisibility(View.VISIBLE);
                replaceFragment(cf);

            }
        });


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
        LocalBroadcastManager.getInstance(this).registerReceiver(displayMintime,
                new IntentFilter("mintime"));


        if (locationFlag == true) {
            finalsourceLocation = new LatLng(sourcelocation.latitude, sourcelocation.longitude);
        } else {
              finalsourceLocation = new LatLng(gpsTrackerLocation.latitude,gpsTrackerLocation.longitude);
        }

        //TODO: RequestButton
        btn_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String s1 = sourceAddress.getText().toString();
                    String d1 = destinationAddr.getText().toString();
                    if (!s1.equals("") && !d1.equals("")) {
                        v1 = v;
                        jbobj = jb.reqObject(MyPref.getString("id", "1024"), MyPref.getString("Contact", "03338983584"), finalsourceLocation.latitude, finalsourceLocation.longitude, destlat, destlang, token, serviceType);
//                    _progdialog.show();
                        skipindex = 0;
                        run(url, v, jbobj, skipindex);
                        btn_req.setClickable(false);
                        btn_req.setEnabled(false);
                        btn_req.setBackgroundColor(Color.GRAY);

                        new CountDownTimer(70000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if (skipindex < 3 && tripflag == false) {
                                    skipindex++;
                                    try {
                                        run(url, v1, jbobj, skipindex);
                                        onStart();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }.start();
                    } else {
                        if (s1.equals("")) {
                            sourceAddress.setError("Enter Source Address");
                        }
                        if (d1.equals("")) {
                            destinationAddr.setError("Enter Destinaton Address");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

               /* Intent intent = new Intent(getApplicationContext(), FeedbackController.class);
                startActivity(intent);
                finish();*/
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

    @Override
    protected void onResume() {

        startService(new Intent(this,
                GetDriverMarkers.class));
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopService(new Intent(this,
                GetDriverMarkers.class));
        super.onPause();
    }

    @Override
    protected void onStart() {

        startService(new Intent(this,
                GetDriverMarkers.class));
        super.onStart();
    }


    @Override
    protected void onStop() {
        stopService(new Intent(this,
                GetDriverMarkers.class));
        super.onStop();
    }

    //getting current Location LatLng for mapReady and tracker button
    private LatLng getMyLocation() {

        //Getting Location Co-ordinates
        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {

            sourceLatitude = gpsTracker.getLatitude();
            sourceLongitude = gpsTracker.getLongitude();
            sourceLatitude1 = sourceLatitude;
            sourceLongitude1 = sourceLongitude;
            gpsTrackerLocation = new LatLng(sourceLatitude, sourceLongitude);
            //    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + sourceLatitude + "\nLong: " + sourceLongitude, Toast.LENGTH_LONG).show();
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
                    mMap.clear();
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
        estdistance.setVisibility(View.VISIBLE);
        estfare.setVisibility(View.VISIBLE);
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
    private void showmin(String mintime)
    {
        nearestAmbtext.setText("Nearest Ambulance is "+ mintime+ " mins away");
    }

    private void showDriver(ArrayList driverList) {
        mMap.clear();
        for (int i = 0; i < driverList.size(); i++) {

            String temp = driverList.get(i).toString();
            String[] latlong = temp.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancemarker));
            markerOptions.position(new LatLng(latitude, longitude));
            mMap.addMarker(markerOptions);

        }


    }

    private void displayAlert(Intent intent) {
        mobile_no = intent.getStringExtra("title");
        latLong = intent.getStringExtra("LATLONG");
        d_token = intent.getStringExtra("drivertoken_no");
        Click_action = intent.getStringExtra("clickaction");
        Trip_id = intent.getStringExtra("Trip_id");

        if (Click_action == null) {
            liner.setVisibility(LinearLayout.GONE);
            Trip_status.setVisibility(View.VISIBLE);
            Trip_status.setText(latLong);
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
            if (latLong.equals("ride Started") || latLong.equals("Reached to Patient") || latLong.equals("Reached Dest and Trip Ended")) {
                trip_panel.setVisibility(View.GONE);
                request_panel.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            } else if (latLong.equals("YOU Have Paid Your Bill ThankYou")) {
                request_panel.setVisibility(View.VISIBLE);
                trip_panel.setVisibility(View.GONE);
                btn_req.setVisibility(View.VISIBLE);
                liner.setVisibility(View.VISIBLE);
                Trip_status.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            } else {
                request_panel.setVisibility(View.GONE);
                trip_panel.setVisibility(View.VISIBLE);
            }

        } else {
            cancel.setVisibility(View.GONE);
            btn_req.setVisibility(View.VISIBLE);
            liner.setVisibility(LinearLayout.VISIBLE);
            Trip_status.setVisibility(View.GONE);
            request_panel.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Driver has Canceled. Please Wait", Toast.LENGTH_LONG).show();
            tripflag = false;
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
        if (id == R.id.action_aman) {
            call_amb("1021");
            return true;
        }
        if (id == R.id.action_chippa) {
            call_amb("1020");
            return true;
        }
        if (id == R.id.action_edhi) {
            call_amb("115");
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
            Intent intent = new Intent(Home.this,HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_policies) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_howitworks) {
            Intent feedback = new Intent(Home.this, FeedbackController.class);
            startActivity(feedback);

        } else if (id == R.id.nav_signout) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            mAuth = null;
            LoginManager.getInstance().logOut();
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
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    public void run(String url, View v, JSONObject jbobj, int skipindex) throws IOException {
        //OkHttpClient client = new OkHttpClient();
        //  LatLng latLng = new LatLng(sourceLatitude, sourceLongitude);
        RequestBody body = RequestBody.create(JSON, jbobj.toString());
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("skipindex", String.valueOf(skipindex));
        String url1 = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url1)
                .post(body)
                .build();
        Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Toast.makeText(getApplicationContext(), "somethng went wrong", Toast.LENGTH_LONG).show();
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
                            cancel.setVisibility(View.VISIBLE);
                            tripflag = true;
//                            _progdialog.cancel();

                        } else {
                            btn_req.setClickable(true);
                            btn_req.setEnabled(true);
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
        gDirectionUrl.append("origin=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
        gDirectionUrl.append("&destination=" + destlat + "," + destlang);
        gDirectionUrl.append("&key=" + "AIzaSyCdJv1f-jv5yU2orVs6TBlFBDZ8kwJJPBI");
        return (gDirectionUrl.toString());
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    private void call_amb(String ph_no) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL); //use ACTION_CALL class
        callIntent.setData(Uri.parse("tel:" + ph_no));    //this is the phone number calling
        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(Home.this,
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        } else {     //have got permission
            try {
                startActivity(callIntent);  //call activity and make phone call
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(Home.this, "yourActivity is not founded", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

