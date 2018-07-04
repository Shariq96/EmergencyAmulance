package com.example.user.emergencyamulance.Models;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.user.emergencyamulance.Controllers.Home;
import com.example.user.emergencyamulance.Controllers.LoginController;

/**
 * Created by farrukh on 25/03/2018.
 */

public class SplashActivity extends AppCompatActivity {
    SharedPreferences myPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        myPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        boolean logger = myPref.getBoolean("login",false);
        if (logger == false) {
            Intent intent = new Intent(getApplicationContext(), LoginController.class);
            startActivity(intent);
            // close splash activity
            finish();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }
        }
}
