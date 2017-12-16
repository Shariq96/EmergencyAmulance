package com.example.user.emergencyamulance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class registeration extends AppCompatActivity {

    EditText name, pass, email, addr;
    Button signup_btn;
    String password, eMail, namee, Jsonobj, hello, mob_no ,addr1 ,mob1;
    JSONObject jbobj;
    public String url = "http://30468d57.ngrok.io/api/useracc/post";
    jbobject jb = new jbobject();
    String Token = FirebaseInstanceId.getInstance().getToken();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        name   = (EditText) findViewById(R.id.e_name);
        pass   = (EditText) findViewById(R.id.e_pass);
        email  = (EditText) findViewById(R.id.e_email);
        addr   = (EditText) findViewById(R.id.e_addr) ;

        mob1 = getIntent().getStringExtra("mobile_no");
        signup_btn = (Button)findViewById(R.id.button2);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namee = name.getText().toString();
                password = pass.getText().toString();
                eMail = email.getText().toString();
                addr1 = addr.getText().toString();
                mob_no = mob1;
                try {
                    jbobj = jb.jb(namee,password,mob_no,addr1,Token);
                    post(url,jbobj);
                } catch (IOException e) {
                    e.printStackTrace();
                }




            }
        });

    }



    OkHttpClient client = new OkHttpClient();

    public void post(String url, JSONObject jbobj) throws IOException {
        RequestBody body = RequestBody.create(JSON,jbobj.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "somethng went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                //  myResponse = myResponse.substring(1, myResponse.length() - 1); // yara
                myResponse = myResponse.replace("\\", "");
                hello = myResponse;
                registeration.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hello.equals("true")) {
                            Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(registeration.this, Main2Activity.class);
                            startActivity(intent);


                        }else if (hello.equals("Acc Already Takes Place")) {
                            Toast.makeText(getApplicationContext(), "Acc Already Takes Place", Toast.LENGTH_LONG).show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "tch  tch Accout Already created", Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        });

    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
}
