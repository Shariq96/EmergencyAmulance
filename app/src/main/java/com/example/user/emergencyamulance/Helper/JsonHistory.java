package com.example.user.emergencyamulance.Helper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.user.emergencyamulance.Controllers.Home.urlwhole;

public class JsonHistory {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    static String json = "";
    public JSONArray array;
    public String url = urlwhole + "/useracc/gethistory";
    JSONObject jsonObj = null;
    OkHttpClient client = new OkHttpClient();

    // constructor
    public JsonHistory() {

    }

    public void initializeData(String id) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("id", id);
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
                String myResponse = response.body().string();
                myResponse = myResponse.replace("\\", "");
                myResponse = myResponse.substring(1, myResponse.length() - 1);
                try {
                    array = new JSONArray(myResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        });
    }
}
