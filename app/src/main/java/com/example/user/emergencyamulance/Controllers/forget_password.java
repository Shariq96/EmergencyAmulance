package com.example.user.emergencyamulance.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emergencyamulance.R;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class forget_password extends AppCompatActivity {

    public String url = "http://192.168.0.102:51967//api/useracc/forgetchangePw";
    private Button btn;
    private EditText re_pass, new_pass;
    private String pass, repass, mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();
        mobile_no = intent.getStringExtra("mobile_no");
        new_pass = (EditText) findViewById(R.id.new_pass);
        re_pass = (EditText) findViewById(R.id.confirm_newpass);


        btn = (Button) findViewById(R.id.submit_newpassword);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = new_pass.getText().toString();
                repass = re_pass.getText().toString();
                if (pass.equals(repass)) {
                    run(pass);
                } else {
                    Toasty.error(getApplicationContext(), "Pass Doesnt Matches!", Toast.LENGTH_LONG, true).show();
                }
            }
        });
    }

    private void run(String pass) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("mobile_no", mobile_no);
        urlBuilder.addQueryParameter("pass", pass);
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
                //  myResponse = myResponse.substring(1, myResponse.length() - 1); // yara
                myResponse = myResponse.replace("\\", "");

                final String finalMyResponse = myResponse;
                forget_password.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalMyResponse.equals("true")) {
                            Toasty.success(forget_password.this, "Congrats Password Changed!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(forget_password.this, LoginController.class));
                            finish();
                        } else {
                            Toasty.error(forget_password.this, "Congrats Password Changed!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(forget_password.this, LoginController.class));
                            finish();
                        }
                    }
                });


            }
        });
    }
}
