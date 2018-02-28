    package com.example.user.emergencyamulance;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.firebase.auth.FirebaseAuth;

    import org.json.JSONObject;

    import java.io.IOException;

    import dmax.dialog.SpotsDialog;
    import okhttp3.Call;
    import okhttp3.Callback;
    import okhttp3.HttpUrl;
    import okhttp3.MediaType;
    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.Response;

    public class loginActivity extends AppCompatActivity {
        EditText mobile_no;
        EditText pass;
        CheckBox sv_pass;
        Button btn_login;
        TextView signup_txt;
        OkHttpClient Client;
        String mobno, password;
        String api_mob, api_pass;

        public String url = "http://724d8461.ngrok.io/api/useracc/get";

        private FirebaseAuth mAuth;
        SharedPreferences.Editor editor;
        SharedPreferences pref;

        public static boolean LogedIn = false;

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
                   Intent signup_intent = new Intent(loginActivity.this, Verifymobile.class);
                   loginActivity.this.startActivity(signup_intent);

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

                    loginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (api_pass.equals("true")) {
                                Toast.makeText(getApplicationContext(), "MainActivity", Toast.LENGTH_LONG).show();
                                editor.putBoolean("login", true);
                                editor.apply();
                                editor.commit();
                                LogedIn = true;
                                Intent intent = new Intent(loginActivity.this, Main2Activity.class);
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

        public static final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();


    }
