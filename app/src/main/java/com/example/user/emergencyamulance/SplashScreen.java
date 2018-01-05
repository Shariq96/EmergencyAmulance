package com.example.user.emergencyamulance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

import static com.example.user.emergencyamulance.loginActivity.LogedIn;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread myThread;
        myThread = new Thread(){
            @Override
            public void run() {
                try {

                    progressbar = (ImageView) findViewById(R.id.view_logo);
                    progressbar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SpotsDialog _progdialog = new SpotsDialog(SplashScreen.this, R.style.Custom);
                            _progdialog.show();
                        }
                    });
                    sleep(20000);
                    // if(LogedIn == true) {
                    //   Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    // startActivity(intent);
                    //finish();
                    // }
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
