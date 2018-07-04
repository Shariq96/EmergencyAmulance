package com.example.user.emergencyamulance.Models;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.user.emergencyamulance.Controllers.Home;
import com.example.user.emergencyamulance.Controllers.LoginController;

/**
 * Created by farrukh on 25/03/2018.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        Intent intent = new Intent(getApplicationContext(), LoginController.class);
        startActivity(intent);
        // close splash activity
        finish();
    }
}
