
package com.example.logintest;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import android.view.View;

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
    private String email02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_in);
        name02 = ((ProfileActivity)ProfileActivity.profileact).returnName();
        id02 = ((ProfileActivity)ProfileActivity.profileact).returnHakbun();
        email02 = ((ProfileActivity)ProfileActivity.profileact).returnEmail();

        getItems();
        //getDataFromAPI();
    }



    private void getItems() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbwjoi01AaB0Boy1gA2pOBn5PatCYTei6rS-HC3xOX9ME7wtnxi7qGqj3eQIZXqT3dJv/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("hiii" + response);
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("hiii2" + error);

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void parseItems(String json) {


        try {

            JSONObject jobj = new JSONObject(json);
            JSONArray jarray = jobj.getJSONArray("items");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String id_excel = jo.getString("id");
                System.out.println(id_excel);
                String name_excel = jo.getString("name");
                String mp_excel = jo.getString("mainpoint");
                String qp_excel = jo.getString("quizpoint");
                String rank_excel = jo.getString("rank");
                //System.out.println("hi" + id_excel);

                if (id_excel.equals(id02)) {
                    TextView n1 = (TextView) findViewById(R.id.name);
                    TextView i1 = (TextView) findViewById(R.id.id);
                    TextView e1 = (TextView) findViewById(R.id.email);
                    TextView r1 = (TextView) findViewById(R.id.rank);
                    TextView m1 = (TextView) findViewById(R.id.mainpoint);
                    TextView q1 = (TextView) findViewById(R.id.quizpoint);

                    n1.setText("name " + name02);
                    i1.setText("id " + id02);
                    e1.setText("email " + email02);
                    r1.setText("rank " + rank_excel);
                    m1.setText("main point " + mp_excel);
                    q1.setText("quiz point " + qp_excel);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void getDataFromAPI() {
        // creating a string variable for URL.
        String url = "https://spreadsheets.google.com/feeds/list/1Kg7Qjdwd-HnxaiRSK9fB0UkrOp0nOvKPSiONb50mEP8/od6/public/values?alt=json";

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
                        String rank_excel = entryObj.getJSONObject("gsx$rank").getString("$t");
                        //userModalArrayList.add(new UserModal(firstName, lastName, email, avatar));

                        System.out.println("heelo" + id02);

                        if(id_excel.equals(id02)){
                            TextView r1 = (TextView)findViewById(R.id.rank);
                            TextView m1 = (TextView)findViewById(R.id.mainpoint);
                            TextView q1 = (TextView)findViewById(R.id.quizpoint);
                            r1.setText("rank " + rank_excel);
                            m1.setText("main point " + mp_excel);
                            q1.setText("quiz point " + qp_excel);
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

    public void reserve_attend(View v){
        Intent intent1 = new Intent(getApplicationContext(), ReserveActivity_attend.class);
        startActivity(intent1);
    }

    public void reserve_quiz(View v){
        Intent intent1 = new Intent(getApplicationContext(), ReserveActivity_quiz.class);
        startActivity(intent1);
    }

    public void refresh(View v)
    {
        //Intent intent1 = new Intent(getApplicationContext(), Showin.class);
        //startActivity(intent1);
        name02 = ((ProfileActivity)ProfileActivity.profileact).returnName();
        id02 = ((ProfileActivity)ProfileActivity.profileact).returnHakbun();
        getItems();
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}
