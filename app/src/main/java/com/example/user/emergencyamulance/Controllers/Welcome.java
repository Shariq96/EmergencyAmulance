package com.example.user.emergencyamulance.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.emergencyamulance.JSONParsing.JsonParsingObject;
import com.example.user.emergencyamulance.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.user.emergencyamulance.Controllers.Home.urlwhole;


public class Welcome extends AppCompatActivity {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public String url = urlwhole + "/useracc/post";
    LoginButton loginButton;
    Button btn;
    CallbackManager callbackManager;
    String email;
     String gender;
    JSONObject jbobj;
     String facebookName;
    String mob1;
    String hello;
    JsonParsingObject jb = new JsonParsingObject();
    String Token = FirebaseInstanceId.getInstance().getToken();
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton)findViewById(R.id.fb_login_Btn);
        mob1 = getIntent().getStringExtra("mobile_no");
        btn = (Button)findViewById(R.id.selfbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_intent = new Intent(Welcome.this, SignUpController.class);
                signup_intent.putExtra("mobile_no",mob1);
                Welcome.this.startActivity(signup_intent);

            }
        });






        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                setProfileToView(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                Intent intent = new Intent(Welcome.this, Home.class);
                startActivity(intent);
                Toast.makeText(Welcome.this, "Welcome SuccessFull ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(Welcome.this, "Welcome Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
            email = jsonObject.getString("email");
            gender=jsonObject.getString("gender");
            facebookName = jsonObject.getString("name");
            jbobj = jb.fbobject(facebookName,"fb",mob1,"fb-Address",Token);
            post(url,jbobj);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                Welcome.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hello.equals("true")) {
                            Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Welcome.this, Home.class);
                            startActivity(intent);


                        } else if (hello.equals("Acc Already Takes Place")) {
                            Toast.makeText(getApplicationContext(), "Acc Already Takes Place", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(Welcome.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
