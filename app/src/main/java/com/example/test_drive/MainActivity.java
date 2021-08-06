package com.example.test_drive;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.lang.Object;
import android.content.Intent;


import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    private String name01;
    private String id01;
    public static Context forstatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forstatic = this;
        //getDataFromAPI();
    }

    public void profile(View v)
    {
        EditText n1 = (EditText) findViewById(R.id.name);
        EditText i1 = (EditText) findViewById(R.id.id);

       name01 = n1.getText().toString();
       id01 = i1.getText().toString();
       Intent intent1 = new Intent(getApplicationContext(), Showin.class);
       startActivity(intent1);
    }

    public String returnName01() {return name01;}
    public String returnId01() {return id01;}
}

