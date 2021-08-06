package com.example.test_drive;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Showin extends AppCompatActivity{

    private String name02;
    private String id02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_in);
        name02 = ((MainActivity)MainActivity.forstatic).returnName01();
        id02 = ((MainActivity)MainActivity.forstatic).returnId01();
        getDataFromAPI();
    }

    private void getDataFromAPI() {
        // creating a string variable for URL.
        String url = "https://spreadsheets.google.com/feeds/list/1v8E3EcVawJsX0US5boll4QYPK_qHm3Tt71ZYY4uVwMo/od6/public/values?alt=json";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(Showin.this);

        // creating a variable for our JSON object request and passing our URL to it.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject feedObj = response.getJSONObject("feed");
                    JSONArray entryArray = feedObj.getJSONArray("entry");
                    for(int i=0; i<entryArray.length(); i++){
                        JSONObject entryObj = entryArray.getJSONObject(i);
                        String id_excel = entryObj.getJSONObject("gsx$id").getString("$t");
                        String name_excel = entryObj.getJSONObject("gsx$name").getString("$t");
                        String mp_excel = entryObj.getJSONObject("gsx$mainpoint").getString("$t");
                        String qp_excel = entryObj.getJSONObject("gsx$quizpoint").getString("$t");
                        //userModalArrayList.add(new UserModal(firstName, lastName, email, avatar));

                        System.out.println("heelo" + id02);

                        if(id_excel.equals(id02)){
                          TextView m1 = (TextView)findViewById(R.id.mainpoint);
                          TextView q1 = (TextView)findViewById(R.id.quizpoint);
                          m1.setText(mp_excel);
                          q1.setText(qp_excel);
                            break;
                        }
                        //System.out.println("heelo" + firstName);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handline on error listener method.
                //Toast.makeText(MainActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        });
        // calling a request queue method
        // and passing our json object
        queue.add(jsonObjectRequest);
    }
}
