package com.example.logintest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;

    //추가된 부분
    EditText editTextLevel;


    Button buttonSignup;
    TextView textviewSingin;
    TextView textviewMessage;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;

    private String name;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class)); //추가해 줄 ProfileActivity
        }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextLevel = (EditText) findViewById(R.id.editTextLevel);



        textviewSingin= (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);

        //button click event
        buttonSignup.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);
    }

    //Firebse creating a new user
    private void registerUser(){
        //사용자가 입력하는 email, password를 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        //추가
        String level=editTextLevel.getText().toString().trim();



        //email과 password가 비었는지 아닌지를 체크 한다.
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(level)){
            Toast.makeText(this, "Level을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }


        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(level).build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                /*Log.d(TAG, "User profile updated.");*/
                                            }
                                        }
                                    });


                            finish();

                            //mingyu
                            name = level;
                            id = email.substring(0,8);
                            addItemToSheet();
                            Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(intent1);
                            //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));


                        } else {
                            //에러발생시
                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                            Toast.makeText(MainActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder myAlertBuilder =
                                    new AlertDialog.Builder(MainActivity.this);
                            // alert의 title과 Messege 세팅
                            myAlertBuilder.setTitle("회원가입 실패");
                            myAlertBuilder.setMessage("사유\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                            // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
                            myAlertBuilder.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int which){
                                    // OK 버튼을 눌렸을 경우
                                    progressDialog.dismiss();

                                }
                            });

                            // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
                            myAlertBuilder.show();


                        }
                        progressDialog.dismiss();
                    }
                });

    }

    //button click event
    @Override
    public void onClick(View view) {
        if(view == buttonSignup) {
            //TODO
            registerUser();
        }

        if(view == textviewSingin) {
            //TODO
            startActivity(new Intent(this, LoginActivity.class)); //추가해 줄 로그인 액티비티
        }
    }
//    public boolean onSupportNavigateUp(){
//        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
//        return super.onSupportNavigateUp(); // 뒤로가기 버튼
//    }


    private void addItemToSheet() {

        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        //final String name = editTextItemName.getText().toString().trim();
        //final String brand = editTextBrand.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbymOkQjVlUZZkUax7ejVLDCGqgeygIOPEBtETQ0XrkiEcWqTSB1mkd9isJaCPQqVbtl/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //loading.dismiss();
                        //Toast.makeText(AddItem.this,response,Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        //startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("id",id);
                parmas.put("name",name);
                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }
}