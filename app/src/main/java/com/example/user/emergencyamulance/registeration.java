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
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import com.facebook.CallbackManager;
        import com.facebook.FacebookActivity;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;
        import com.facebook.login.widget.ProfilePictureView;
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
public class registeration extends AppCompatActivity {

    EditText name, pass, email, phone;
    ImageView signup_btn,fb;
    String password, eMail, namee, Jsonobj, hello, mob_no ,addr1 ,mob1;
    String emmail;
    String gender;
    String facebookName;
    LoginButton loginButton;
    Button btn;
    CallbackManager callbackManager;
        JSONObject jbobj;
    public String url = "http://192.168.0.103:51967/api/useracc/post";
    jbobject jb = new jbobject();
    String Token = FirebaseInstanceId.getInstance().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_registeration);
        loginButton = (LoginButton)findViewById(R.id.fb_login_Btn);
        mob1 = getIntent().getStringExtra("mobile_no");
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
                Intent intent = new Intent(registeration.this, Main2Activity.class);
                startActivity(intent);
                Toast.makeText(registeration.this, "MainActivity SuccessFull ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(registeration.this, "MainActivity Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });



        name   = (EditText) findViewById(R.id.txt_Username);
        pass   = (EditText) findViewById(R.id.txt_Password);
        email  = (EditText) findViewById(R.id.txt_Email);
        phone   = (EditText) findViewById(R.id.txt_Phone) ;

        mob1 = getIntent().getStringExtra("mobile_no");

        signup_btn = (ImageView) findViewById(R.id.btn_reg_me);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namee = name.getText().toString();
                password = pass.getText().toString();
                eMail = email.getText().toString();
                addr1 = phone.getText().toString();
                mob_no = mob1;
                try {
                    jbobj = jb.jb(namee,password,mob_no,addr1,Token);
                    post(url,jbobj);
                } catch (IOException e) {
                    e.printStackTrace();
                }




            }
        });

    } @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
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
                Toast.makeText(getApplicationContext(), "Something went Wrong, Try Again!", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getApplicationContext(), "Registered!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(registeration.this, Main2Activity.class);
                            startActivity(intent);


                        }else if (hello.equals("Acc Already Takes Place")) {
                            Toast.makeText(getApplicationContext(), "Acc Already Takes Place", Toast.LENGTH_LONG).show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Already Exist, Sign-in Please", Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        });

    } private void setProfileToView(JSONObject jsonObject) {
        try {
            emmail = jsonObject.getString("email");
            gender=jsonObject.getString("gender");
            facebookName = jsonObject.getString("name");
            jbobj = jb.fbobject(facebookName,"fb",mob1,"fb-Address",Token);
            postfb(url,jbobj);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    OkHttpClient client = new OkHttpClient();

    public void postfb(String url, JSONObject jbobj) throws IOException {
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


                        } else if (hello.equals("Acc Already Takes Place")) {
                            Toast.makeText(getApplicationContext(), "Acc Already Takes Place", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(registeration.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
}
