package com.example.user.emergencyamulance;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
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

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, NavigationView.OnNavigationItemSelectedListener, FragmentChangeListner, android.location.LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest request;
    private Location lastLocation;
    private PlaceAutocomplete places;
    private Location myloc;
    private Marker currentLocation;
    public static Button btn_req, btn_cancel;
    public static final int REQUEST_LOCATION_CODE = 99;
    SupportMapFragment mapFragment;
    public static Boolean Status = false;
    public boolean first_time = true;
    public static String mobile_no, latLong, d_token, Trip_id, Click_action;
    private static EditText editText;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private double destlang, destlat;

    String hello;
    String url = "http://724d8461.ngrok.io/api/useracc/GetRequest";
    String token = FirebaseInstanceId.getInstance().getToken();
    SpotsDialog _progdialog;
    public static FrameLayout f1;
    CancelationFragment cf = new CancelationFragment();

    int placePicker_req = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_drawer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                1000, this);
        View Locatiob_Button = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent())
                .findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) Locatiob_Button.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 200);

        mapFragment.getMapAsync(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMsgReciver,
                new IntentFilter("myFunction"));
        //  f1 = (FrameLayout)findViewById(R.id.frame);
        //    _progdialog = new SpotsDialog(Main2Activity.this, R.style.Custom);


        btn_cancel = (Button) findViewById(R.id.btn_cncel);
        btn_cancel.setVisibility(View.GONE);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1.setVisibility(v.VISIBLE);
                replaceFragment(cf);
            }
        });
        editText = (EditText) findViewById(R.id.togo);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = intentBuilder.build(Main2Activity.this);
                    startActivityForResult(intent, placePicker_req);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_req = (Button) findViewById(R.id.btn_req);
        btn_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    _progdialog.show();
                    run(url, v);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == placePicker_req) {
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
                    String address = String.format("Place : %s", place.getAddress());
                    LatLng destloc = place.getLatLng();
                    destlang = destloc.longitude;
                    destlat = destloc.latitude;
                    editText.setText(address);
                    setDestMarker(destlang, destlat);


                } else {
                    Toast.makeText(this, "Please Select Appropriate Location", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setDestMarker(double lang, double lat) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lang));
        markerOptions.title("Destination");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        float result[] = new float[10];
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), lat, lang, result);
        markerOptions.snippet("Distance = " + result[0]);
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(-2));
        setDirections();

    }
    private void setDirections()
    {   Object[] dataTransfer = new Object[3];
        String url = getDirectionUrl();
        GetDirectionData gdd = new GetDirectionData();
        dataTransfer[0] =mMap;
        dataTransfer[1]= url;
        dataTransfer[2] = new LatLng(destlat,destlang);
        gdd.execute(dataTransfer);

    }

    /*public void alliswell() {
        f1.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        btn_req.setVisibility(View.VISIBLE);

    } */

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMsgReciver);
        super.onDestroy();
    }

    private BroadcastReceiver mMsgReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Status = true;
            displayAlert(intent);
        }
    };

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
            // Handle the camera action
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_policies) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_howitworks) {

        } else if (id == R.id.nav_signout) {
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Main2Activity.this, loginActivity.class));
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

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        locationManager.removeUpdates(this);
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
        MarkerOptions options = new MarkerOptions();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setTrafficEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(true);


            }
        } else {
            googleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);

        }
    }

    protected synchronized void googleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
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

    public void CalcDistance(LatLng[] ltlong) {
        String result = null;
        float temp = 0;
        Location loc1 = new Location("");
        loc1.setLatitude(myloc.getLatitude());
        loc1.setLongitude(myloc.getLongitude());
        for (int i = 0; i != ltlong.length; i++) {
            Location loc2 = new Location("");
            loc2.setLatitude(ltlong[i].latitude);
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
    }

    OkHttpClient Client = new OkHttpClient();

    public void run(String url, View v) throws IOException {
        // OkHttpClient client = new OkHttpClient();
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("mobile_no", "3338983584");
        urlBuilder.addQueryParameter("lat", String.valueOf(lastLocation.getLatitude()));
        urlBuilder.addQueryParameter("longi", String.valueOf(lastLocation.getLongitude()));
        urlBuilder.addQueryParameter("token", token);
        String url1 = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url1)
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

                Main2Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hello.equals("true")) {
                            Toast.makeText(getApplicationContext(), "Got it", Toast.LENGTH_LONG).show();
                            btn_req.setVisibility(View.GONE);
                            btn_cancel.setVisibility(View.VISIBLE);
                            _progdialog.cancel();

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
        gDirectionUrl.append("origin="+currentLocation.getLatitude()+","+currentLocation.getLongitude());
        gDirectionUrl.append("&destination="+destlat+","+destlang);
        gDirectionUrl.append("&key="+"AIzaSyB5WDX6S95k_KvAdN7PjdXzz9XIneDhIsc");
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
