package com.example.user.emergencyamulance.Controllers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.emergencyamulance.Helper.FragmentChangeListner;
import com.example.user.emergencyamulance.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.user.emergencyamulance.Controllers.Home.Status;
import static com.example.user.emergencyamulance.Controllers.Home.Trip_id;
import static com.example.user.emergencyamulance.Controllers.Home.btn_cancel;
import static com.example.user.emergencyamulance.Controllers.Home.btn_req;
import static com.example.user.emergencyamulance.Controllers.Home.d_token;
import static com.example.user.emergencyamulance.Controllers.Home.f1;
import static com.example.user.emergencyamulance.Controllers.Home.urlwhole;


public class CancelTrip extends Fragment implements FragmentChangeListner {
    View v;
    Button btn;
    RadioButton radioReasonBtn;
    RadioGroup radioReasongGrp;
    int selectedId;
    String val;
    String url = urlwhole + "useracc/cancelRideUser";
    /*    public  void onClickListnerButton()
        {
            Home main2Activity = new Home();
            main2Activity.alliswell();
        }*/
    OkHttpClient Client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cancelation, container, false);
        radioReasongGrp = (RadioGroup)v.findViewById(R.id.rg);
        btn = (Button)v.findViewById(R.id.btn_submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Status == true) {
                    int selected_Id = radioReasongGrp.getCheckedRadioButtonId();
                    radioReasonBtn = (RadioButton) getView().findViewById(selected_Id);
                    Toast.makeText(getActivity().getApplicationContext(), radioReasonBtn.getText().toString(), Toast.LENGTH_SHORT).show();
                    f1.setVisibility(View.GONE);
                    btn_cancel.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "YOUR RIDE IS CANCELED", Toast.LENGTH_LONG).show();
                    btn_req.setVisibility(View.VISIBLE);
                    try {
                        post(url, radioReasonBtn.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else  {
                    int selected_Id = radioReasongGrp.getCheckedRadioButtonId();
                    radioReasonBtn = (RadioButton) getView().findViewById(selected_Id);
                    Toast.makeText(getActivity().getApplicationContext(), radioReasonBtn.getText().toString(), Toast.LENGTH_SHORT).show();
                    f1.setVisibility(View.GONE);
                    btn_cancel.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "Please Be sure when booking a ride", Toast.LENGTH_LONG).show();
                    btn_req.setVisibility(View.VISIBLE);
                }

            }
        });
        return  v;

    }

    public void post(String url, String val) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("Trip_id",Trip_id);
        urlBuilder.addQueryParameter("cancelOption", val);
        urlBuilder.addQueryParameter("token",d_token);
        //urlBuilder.addQueryParameter("Status_id","1");
        String url1 = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url1)
                .build();
       /* RequestBody body = RequestBody.create(JSON,jbobj.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();*/
        Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity().getApplicationContext(), "somethng went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                //  myResponse = myResponse.substring(1, myResponse.length() - 1); // yara
                myResponse = myResponse.replace("\\", "");
                final String hello = myResponse;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hello.equals("true")) {
                            Toast.makeText(getActivity().getApplicationContext(),"RideCanceled",Toast.LENGTH_LONG).show();
                         } else {
                            Toast.makeText(getActivity().getApplicationContext(), "tch tch", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });

    }

    @Override
    public void replaceFragment(Fragment fragment) {
        CancelTrip cf = new CancelTrip();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(cf);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.commit();
    }
}
