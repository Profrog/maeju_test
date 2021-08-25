package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CheckActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        btn_check = (Button) findViewById(R.id.btn_checkout);
        btn_check.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btn_check){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
