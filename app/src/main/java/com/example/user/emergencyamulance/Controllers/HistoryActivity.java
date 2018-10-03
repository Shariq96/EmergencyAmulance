    package com.example.user.emergencyamulance.Controllers;

    import android.app.ProgressDialog;
    import android.content.SharedPreferences;
    import android.location.Address;
    import android.location.Geocoder;
    import android.os.AsyncTask;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.widget.ListView;

    import com.example.user.emergencyamulance.Helper.JsonHistory;
    import com.example.user.emergencyamulance.JSONParsing.historyClass;
    import com.example.user.emergencyamulance.R;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Locale;

    import okhttp3.MediaType;

    import static com.example.user.emergencyamulance.Controllers.Home.urlwhole;

    public class HistoryActivity extends AppCompatActivity {
        public static final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        ProgressDialog pDialog;
        JSONArray array;
        private RecyclerView rv;
        SharedPreferences myPref;
        private ListView historylist;
        public String url = urlwhole + "/useracc/gethistory";
        private String id;
        JSONObject jsonObj;
        JsonHistory jh = new JsonHistory();
        CustomAdapter adapter;
        Geocoder geocoder;
        List<Address> saddresses = new ArrayList<>();
        List<Address> daddresses = new ArrayList<>();
        private List<historyClass> persons = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);
            rv = (RecyclerView) findViewById(R.id.rv);

            LinearLayoutManager llm = new LinearLayoutManager(this);
            rv.setLayoutManager(llm);
            rv.setHasFixedSize(true);
            myPref = getSharedPreferences("MyPref", MODE_PRIVATE);
            id = myPref.getString("id", null);

            geocoder = new Geocoder(this, Locale.getDefault());
            new myAsync().execute();

        }


        public void initializeAdapter() {

            adapter = new CustomAdapter(persons);
            rv.setAdapter(adapter);
        }

        class myAsync extends AsyncTask<String, String, JSONArray> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(HistoryActivity.this);
                pDialog.setMessage("Getting Data ...");
                pDialog.setIndeterminate(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {

                pDialog.dismiss();
                initializeAdapter();
            }

            @Override
            protected JSONArray doInBackground(String... strings) {
                try {
                    jh.initializeData(id);
                    Thread.sleep(10000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                array = jh.array;
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {


                        try {
                            jsonObj = array.getJSONObject(i);
                            String[] source = jsonObj.getString("Source").split("-");
                            saddresses = geocoder.getFromLocation(Double.valueOf(source[0]), Double.valueOf(source[1]), 1);
                            String[] dest = jsonObj.getString("Destination").split("-");
                            daddresses = geocoder.getFromLocation(Double.valueOf(dest[0]), Double.valueOf(dest[1]), 1);
                            persons.add(new historyClass(jsonObj.getString("time"),
                                    saddresses.get(0).getAddressLine(0),
                                    daddresses.get(0).getAddressLine(0),
                                    jsonObj.getString("FinalFare"),
                                    jsonObj.getString("DriverName"),
                                    jsonObj.getString("Vehicle_type"),
                                    jsonObj.getString("Vehicle_plate")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                } else {
                }
                return array;
            }
        }
    }