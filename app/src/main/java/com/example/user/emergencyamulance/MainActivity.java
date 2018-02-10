package com.example.user.emergencyamulance;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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


public class MainActivity extends AppCompatActivity  {
    LoginButton loginButton;
    Button btn;
    CallbackManager callbackManager;
    String email;
     String gender;
    JSONObject jbobj;
     String facebookName;
    String mob1;
    String hello;
    public String url = "http://192.168.1.105/api/useracc/post";
    jbobject jb = new jbobject();
    String Token = FirebaseInstanceId.getInstance().getToken();

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
                Intent signup_intent = new Intent(MainActivity.this, registeration.class);
                signup_intent.putExtra("mobile_no",mob1);
                MainActivity.this.startActivity(signup_intent);

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
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Login SuccessFull ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login Canceled", Toast.LENGTH_SHORT).show();
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
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hello.equals("true")) {
                            Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                            startActivity(intent);


                        } else if (hello.equals("Acc Already Takes Place")) {
                            Toast.makeText(getApplicationContext(), "Acc Already Takes Place", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
}
