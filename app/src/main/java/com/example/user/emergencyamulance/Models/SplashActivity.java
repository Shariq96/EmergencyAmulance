package com.example.user.emergencyamulance.Models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.user.emergencyamulance.Controllers.Home;
import com.example.user.emergencyamulance.Controllers.LoginController;

import javax.xml.datatype.Duration;

import es.dmoral.toasty.Toasty;

/**
 * Created by farrukh on 25/03/2018.
 */

public class SplashActivity extends AppCompatActivity {
    SharedPreferences myPref;
    private boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {

            myPref = getSharedPreferences("MyPref", MODE_PRIVATE);
            boolean logger = myPref.getBoolean("login", false);
            if (logger == false) {


                Intent intent = new Intent(getApplicationContext(), LoginController.class);
                startActivity(intent);
                // close splash activity
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }

        }



    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();


    }


    @Override
    protected void onResume() {
        myPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        boolean logger = myPref.getBoolean("login", false);
        if (logger == false) {


            Intent intent = new Intent(getApplicationContext(), LoginController.class);
            startActivity(intent);
            // close splash activity
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }

        super.onResume();

    }
}
