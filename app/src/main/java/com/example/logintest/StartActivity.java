package com.example.logintest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonSignup;
    Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        buttonSignup = (Button) findViewById(R.id.mvtosignup);
        buttonLogin = (Button) findViewById(R.id.mvtologin);

        buttonSignup.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

    }

    //button click event
    @Override
    public void onClick(View view) {
        if (view == buttonSignup) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        if (view == buttonLogin) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}


