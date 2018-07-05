package com.example.user.emergencyamulance.Controllers;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.CountDownTimer;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.user.emergencyamulance.R;
    import com.google.firebase.auth.FirebaseAuth;

    import java.io.IOException;

    import es.dmoral.toasty.Toasty;
    import okhttp3.Call;
    import okhttp3.Callback;
    import okhttp3.HttpUrl;
    import okhttp3.MediaType;
    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.Response;

public class LoginController extends AppCompatActivity {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static boolean LogedIn = false;
    public String url = "http://192.168.0.103:51967//api/useracc/get";
        EditText mobile_no;
        EditText pass;
        CheckBox sv_pass;
        TextView forgetpass;
        Button btn_login;
        TextView signup_txt;
        OkHttpClient Client;
        String mobno, password;
        String api_mob, api_pass;
        SharedPreferences.Editor editor;
        SharedPreferences pref;
    OkHttpClient client = new OkHttpClient();
    private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);


            boolean var = Verifymobile.getVariable();
            mAuth = FirebaseAuth.getInstance();

            mobile_no = (EditText) findViewById(R.id.etext1);
            pass = (EditText) findViewById(R.id.etext2);
            sv_pass = (CheckBox) findViewById(R.id.chk_pass);
            btn_login = (Button) findViewById(R.id.button);
            signup_txt = (TextView) findViewById(R.id.textView2);

            signup_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signup_intent = new Intent(LoginController.this, SignUpController.class);
                    LoginController.this.startActivity(signup_intent);

               }
            });

            forgetpass = findViewById(R.id.forgetpass);

            forgetpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent forget = new Intent(LoginController.this, Verifymobile.class);
                    LoginController.this.startActivity(forget);
                }
            });

            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            editor = pref.edit();
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // try {
                    Intent forget = new Intent(LoginController.this, Home.class);
                    LoginController.this.startActivity(forget);
                    //     mobno = mobile_no.getText().toString();
                    //    password = pass.getText().toString();
                    //   run();

                    //  } catch (IOException e) {
                    //     e.printStackTrace();
                    //  }
                    CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            Toasty.info(getApplicationContext(), "Login Sucessful", Toast.LENGTH_LONG, true).show();
                        }

                        @Override
                        public void onFinish() {
                            Toasty.success(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG, true).show();
                        }
                    };

                    countDownTimer.start();


                }


            });

        }

        private void run() throws IOException {

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("mobile_no", mobno);
            urlBuilder.addQueryParameter("password",password);
            String url1 = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url1)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }


                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    String myResponse = response.body().string();
                    //  myResponse = myResponse.substring(1, myResponse.length() - 1); // yara
                    myResponse = myResponse.replace("\\", "");

                   //SONObject jarray = null;
                     //jarray = new JSONObject(myResponse);
                    //api_pass = jarray.getString("password");
                    api_pass = myResponse;
                    //api_pass = jarray.getJSONObject(0).getString("password");

                    LoginController.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (api_pass.equals("true")) {
                                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                                editor.putBoolean("login", true);
                                editor.apply();
                                editor.commit();
                                LogedIn = true;
                                Intent intent = new Intent(LoginController.this, Home.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();

                                editor.putBoolean("login", false);
                                editor.commit();
                            }
                        }

                    });


                }


            });

        }


    }
