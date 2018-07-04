package com.example.user.emergencyamulance.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.emergencyamulance.R;

public class cardview extends AppCompatActivity {

    TextView driverName;
    TextView source;
    TextView dest;
    TextView fare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);

        driverName = (TextView)findViewById(R.id.Drivername);
        source = (TextView)findViewById(R.id.source1);
        dest = (TextView) findViewById(R.id.Dest1);
        fare = (TextView) findViewById(R.id.time);


    }
}
