package com.example.user.emergencyamulance.Controllers;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.user.emergencyamulance.JSONParsing.JsonParsingObject;
import com.example.user.emergencyamulance.Models.FeedBack;
import com.example.user.emergencyamulance.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FeedbackController extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Toasty.info(getApplicationContext(), "Saving", Toast.LENGTH_LONG, true).show();
            }

            @Override
            public void onFinish() {
                Toasty.success(getApplicationContext(), "Thank You", Toast.LENGTH_LONG, true).show();
            }
        };

        countDownTimer.start();

    }
}
