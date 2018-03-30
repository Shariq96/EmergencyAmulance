package com.example.user.emergencyamulance.Models;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.user.emergencyamulance.Controllers.FeedbackController;
import com.example.user.emergencyamulance.JSONParsing.JsonParsingObject;
import com.example.user.emergencyamulance.R;

import org.json.JSONObject;

import java.io.IOException;


import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by farrukh on 30/03/2018.
 */

public class FeedBack extends AppCompatActivity {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient Client = new OkHttpClient();
    JsonParsingObject jbobj = new JsonParsingObject();
    JSONObject jsonObject;
    private RatingBar ratingExperience;
    private EditText comments;
    private RatingBar rateDriver;
    private Button btnSubmit;
    private String responseString;
    private String url;


    public void collectFeedbackofUser() {

        rateDriver = findViewById(R.id.rateDriver);
        ratingExperience = findViewById(R.id.rateExperience);
        comments = findViewById(R.id.comments);
        btnSubmit = findViewById(R.id.submit_feedback);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   float driverRating = rateDriver.getRating();
                float experienceRating = ratingExperience.getRating();
                String commentofUser = comments.toString();
*/





                /*  For Testing Purpose
                try {
                    jsonObject = jbobj.collectDriverFeedback("Sharik","001","D-001",commentofUser,driverRating);
                    updateDatabase(url, v,jsonObject);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }


    private void updateDatabase(String url, View view, JSONObject jsonObj) throws IOException {

        RequestBody body = RequestBody.create(JSON, jsonObj.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "somethng went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String myResponse = response.body().string();
                myResponse = myResponse.replace("\\", "");
                responseString = myResponse;

                FeedBack.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseString.equals("true")) {
                            Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                });
            }
        });
    }


}
