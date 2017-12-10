package com.example.user.emergencyamulance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {

                        sleep(3000);
                        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                        startActivity(intent);
                        finish();

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

    }

}
