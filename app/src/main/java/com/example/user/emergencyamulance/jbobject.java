package com.example.user.emergencyamulance;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 12/6/2017.
 */

public class jbobject
{
    public JSONObject jb (String Customer_name,String pass, String Contact_No,String Address,String token_no)
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Customer_name", Customer_name);
            obj.put("pass", pass);
            obj.put("Contact_No", Contact_No);
            obj.put("Address", Address);
            obj.put("token_no", token_no);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  obj;
    }
    public JSONObject fbobject(String Customer_name,String pass, String Contact_No,String Address,String token_no)
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("Customer_name", Customer_name);
            obj.put("pass", pass);
            obj.put("Contact_No", Contact_No);
            obj.put("Address", Address);
            obj.put("token_no", token_no);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  obj;
    }
    }
