package com.example.user.emergencyamulance.JSONParsing;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.user.emergencyamulance.Controllers.Home;
import com.example.user.emergencyamulance.Helper.myApplication;
import com.example.user.emergencyamulance.Models.GPSTracker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.user.emergencyamulance.Controllers.Home.sourceLatitude1;
import static com.example.user.emergencyamulance.Controllers.Home.sourceLongitude1;

/**
 * Created by User on 2/23/2018.
 */

public class GetDriverMarkers extends Service {
    OkHttpClient client = new OkHttpClient();

    GPSTracker  gpsTracker = new GPSTracker(this);

    Timer mTimer;
    public String url = "http://192.168.0.102:51967/api/useracc/getDriver";
    Timer timer;
    public String url1 = "http://192.168.0.102:51967/api/useracc/getNearestTime";
    TimerTask timerTask1 = new TimerTask() {
        @Override
        public void run() {



            HttpUrl.Builder urlBuilder = HttpUrl.parse(url1).newBuilder();
            urlBuilder.addQueryParameter("lat", String.valueOf(sourceLatitude1));
            urlBuilder.addQueryParameter("lng", String.valueOf(sourceLongitude1));
            String url1 = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url1)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String myResponse = response.body().string().toString();
                    myResponse = myResponse.substring(1, myResponse.length()-1);
                    if(myApplication.isActivityVisible()) {
                        Intent intent = new Intent("mintime");
                        intent.putExtra("minTime", myResponse);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }

                }
            });


        }
    };
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            String url1 = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url1)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ArrayList<String> list=new ArrayList<String>();

                    String myResponse = response.body().string().toString();
                    try {
                        JSONArray jsonArray = new JSONArray(myResponse);
                        JSONObject jsonObject;
                        for (int i = 0; i < jsonArray.length() ; i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            String latlong = jsonObject.getString("Altitude");
                            list.add(latlong);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(myApplication.isActivityVisible()) {
                        Intent intent = new Intent("dd");
                        intent.putExtra("list", list);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                    // JSONObject jsonObject = new JSONObject(myResponse)

                }
            });

        }
    };
    @Override
    public void onCreate() {
        mTimer = new Timer();
        mTimer.schedule(timerTask, 10000, 30 * 1000);
        timer = new Timer();
        timer.schedule(timerTask1,10000,30*1000);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        this.mTimer.cancel();
        this.timer.cancel();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }
}