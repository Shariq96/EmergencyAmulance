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
    import com.google.firebase.iid.FirebaseInstanceId;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.net.HttpURLConnection;

    import es.dmoral.toasty.Toasty;
    import okhttp3.Call;
    import okhttp3.Callback;
    import okhttp3.HttpUrl;
    import okhttp3.MediaType;
    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.Response;

    import static com.example.user.emergencyamulance.Controllers.Home.urlwhole;

public class LoginController extends AppCompatActivity {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static boolean LogedIn = false;
    public String url = urlwhole + "/useracc/get";
        EditText mobile_no;
        EditText pass;
        CheckBox sv_pass;
    String pass1,name,id,contact,token;
        TextView forgetpass;
        Button btn_login;
        TextView signup_txt;
        OkHttpClient Client;
        String mobno, password;
        String api_mob, api_pass;
        SharedPreferences.Editor editor;
        SharedPreferences pref;
    JSONArray array = null;
    JSONObject jsonObj = null;

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
                    Intent signup_intent = new Intent(LoginController.this, Verifymobile.class);
                    LoginController.this.startActivity(signup_intent);

               }
            });

            forgetpass = findViewById(R.id.forgetpass);

            forgetpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent forget = new Intent(LoginController.this, verifyForPass.class);
                    startActivity(forget);

                }
            });

            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            editor = pref.edit();
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mobno = mobile_no.getText().toString();
                        password = pass.getText().toString();
                        run();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            token = FirebaseInstanceId.getInstance().getToken();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addQueryParameter("mobile_no", mobno);
            urlBuilder.addQueryParameter("password",password);
            urlBuilder.addQueryParameter("token", token);
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
                    try {
                         array= new JSONArray(myResponse);
                         jsonObj = array.getJSONObject(0);
                        pass1 = jsonObj.getString("pass");
                         name = jsonObj.getString("Customer_name");
                        id = jsonObj.getString("Customer_id");
                        contact = jsonObj.getString("Contact_No");
                         token = jsonObj.getString("token_no");
                        String new1 = pass1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    LoginController.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (password.equals(pass1)) {
                                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                                editor.putBoolean("login", true);
                                editor.putString("name",name);
                                editor.putString("contact",contact);
                                editor.putString("id",id);
                                editor.putString("token",token);
                                editor.apply();
                                editor.commit();
                                LogedIn = true;
                                Intent intent = new Intent(LoginController.this, Home.class);
                                startActivity(intent);
                                finish();
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
