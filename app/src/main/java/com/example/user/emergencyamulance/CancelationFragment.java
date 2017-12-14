package com.example.user.emergencyamulance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by User on 12/11/2017.
 */

public class CancelationFragment extends Fragment {
    View v;
    Button btn;
    RadioButton btn1,btn2,btn3,btn4,btn5;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cancelation,container,false);
        btn = (Button)v.findViewById(R.id.btn_sumbit);
        btn1 =(RadioButton)v.findViewById(R.id.radioButton1);
        btn2 =(RadioButton)v.findViewById(R.id.radioButton2);
        btn3 =(RadioButton)v.findViewById(R.id.radioButton3);
        btn4 =(RadioButton)v.findViewById(R.id.radioButton4);
        btn5 =(RadioButton)v.findViewById(R.id.radioButton5);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return  v;
    }
}
