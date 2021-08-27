package com.example.logintest;
import android.app.Activity;
import android.content.Context;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

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
import java.util.Collections;
import java.util.List;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ProfileActivity";

    public static Context profileact;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;



    //추가
    //firebase data
    private String name="";
    private String email="";
    private String hakbun="";

    //drive data
    private String name02;
    private String id02;
    private String email02;

    // 인증 버튼
    Button addSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileact=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing views
        //textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Name, email address
                name = profile.getDisplayName();
                email = profile.getEmail();
            }
        }
        hakbun=email.substring(0,8);
        String test="테스트";

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행

        while(name==null){
            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    // Name, email address
                    name = profile.getDisplayName();
                    email = profile.getEmail();

                }
            }

        }


        // 주차 인증하기
        addSubmit=(Button)findViewById(R.id.btn_submit);

        // addSubmit 화면 전환
        addSubmit.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(),SubmitActivity.class);
            startActivity(intent);
        });

        //move to show int
        name02 = name;
        id02 = hakbun;
        email02 = email;
        getItems();
    }

    @Override
    public void onClick(View view) {

    }
    @Override public void onBackPressed() { }


    /*public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }*/


    public void enter(View v)
    {
        Intent intent1 = new Intent(getApplicationContext(), Showin.class);
        startActivity(intent1);
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
                    //TextView i1 = (TextView) findViewById(R.id.id);
                    TextView e1 = (TextView) findViewById(R.id.email);
                    //TextView r1 = (TextView) findViewById(R.id.rank);
                    TextView m1 = (TextView) findViewById(R.id.mainpoint);
                    TextView q1 = (TextView) findViewById(R.id.quizpoint);

                    n1.setText("name " + name02);
                    //i1.setText("id " + id02);
                    e1.setText("email " + email02);
                    //r1.setText("rank " + rank_excel);
                    m1.setText(mp_excel);
                    q1.setText(qp_excel);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //showin

    private void getDataFromAPI() {
        // creating a string variable for URL.
        String url = "https://spreadsheets.google.com/feeds/list/1Kg7Qjdwd-HnxaiRSK9fB0UkrOp0nOvKPSiONb50mEP8/od6/public/values?alt=json";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

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
                            //TextView r1 = (TextView)findViewById(R.id.rank);
                            TextView m1 = (TextView)findViewById(R.id.mainpoint);
                            TextView q1 = (TextView)findViewById(R.id.quizpoint);
                            //r1.setText("rank " + rank_excel);
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


    public void set(View v){
        Intent intent1 = new Intent(getApplicationContext(), alarmActivity.class);
        startActivity(intent1);
    }

    public void guide(View v)
    {
        Intent intent1 = new Intent(getApplicationContext(), Guidein.class);
        startActivity(intent1);
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
                        System.out.println("data" + error);
                        Toast.makeText(getApplicationContext(),"매주 1과제 드라이브 접속이 실패하였습니다. 상담채널로 문의",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    //showin

    public void refresh(View v)
    {
        name02 = ((ProfileActivity)ProfileActivity.profileact).returnName();
        id02 = ((ProfileActivity)ProfileActivity.profileact).returnHakbun();
        getItems();
    }

    public void goprofile(View v)
    {
        Intent intent1 = new Intent(getApplicationContext(), DetailActivity.class);
        startActivity(intent1);
    }


    //이름,학번 리턴함수
    public String returnName(){return name;}
    public String returnHakbun(){return hakbun;}
    public String returnEmail(){return email;}

}