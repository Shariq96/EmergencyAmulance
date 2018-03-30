package com.example.user.emergencyamulance.JSONParsing;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.user.emergencyamulance.Helper.myApplication;

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

/**
 * Created by User on 2/23/2018.
 */

public class GetDriverMarkers extends Service {
    public String url = "http://192.168.0.103:51967/api/useracc/getDriver";
    OkHttpClient client = new OkHttpClient();
    Timer mTimer;
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
        mTimer.schedule(timerTask, 10000, 40 * 1000);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

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
