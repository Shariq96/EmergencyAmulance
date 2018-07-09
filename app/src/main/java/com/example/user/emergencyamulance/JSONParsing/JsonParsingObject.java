package com.example.user.emergencyamulance.JSONParsing;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 12/6/2017.
 */

public class JsonParsingObject
{
    public JSONObject reqObject(String id, String Contact_No, double lat, double longi, double destlat, double destlong, String token_no, String Service_type)
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",id);
            obj.put("mobile_no", Contact_No);
            obj.put("lat", lat);
            obj.put("longi", longi);
            obj.put("token", token_no);
            obj.put("destlat", destlat);
            obj.put("destlong", destlong);
            obj.put("Service_type", Service_type);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  obj;
    }

    public JSONObject collectDriverFeedback(String Customer_name, String user_id, String driver_id, String comments, float rating) {

        JSONObject object = new JSONObject();
        try {
            object.put("UserId", user_id);
            object.put("DriverId", driver_id);
            object.put("Customer_name", Customer_name);
            object.put("Comments", comments);
            object.put("Rating", rating);
        } catch (JSONException e) {
            e.fillInStackTrace();
        }
        return object;

    }

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
