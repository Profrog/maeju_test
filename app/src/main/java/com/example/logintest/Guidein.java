package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class Guidein extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_in);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://kgr9911.wixsite.com/everyweekhomework");
    }
}
