package com.example.user.emergencyamulance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by farrukh on 25/03/2018.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(intent);
        // close splash activity
        finish();
    }
}
