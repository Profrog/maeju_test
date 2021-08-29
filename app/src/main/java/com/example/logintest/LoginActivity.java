package com.example.logintest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignin;
    ImageButton gotomain;
    TextView textviewSingin;
    TextView textviewMessage;
    TextView textviewFindPassword;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.login_activity);

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
       /* textviewSingin= (TextView) findViewById(R.id.textViewSignin);*/
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        textviewFindPassword = (TextView) findViewById(R.id.textViewFindpassword);
        buttonSignin = (Button) findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);
        gotomain=(ImageButton) findViewById(R.id.gotomain);

        //button click event
        gotomain.setOnClickListener(this);
        buttonSignin.setOnClickListener(this);
//        textviewSingin.setOnClickListener(this);
        textviewFindPassword.setOnClickListener(this);


    }

    //firebase userLogin method
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.print(task.isSuccessful());
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);

                        }
                        else{
                            AlertDialog.Builder myAlertBuilder =
                                    new AlertDialog.Builder(LoginActivity.this);
                            // alert의 title과 Messege 세팅
                            myAlertBuilder.setTitle("로그인 실패");
                            myAlertBuilder.setMessage("로그인에 실패하였습니다.");
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
                    }
                });

    }



    @Override
    public void onClick(View view) {
        if(view == buttonSignin) {
            userLogin();
        }
//        if(view == textviewSingin) {
//            finish();
//            startActivity(new Intent(this, MainActivity.class));
//        }
        if(view == textviewFindPassword) {
            finish();
            startActivity(new Intent(this, FindActivity.class));
        }
        if(view==gotomain){
            startActivity(new Intent(this,StartActivity.class));
        }
    }
    @Override public void onBackPressed() { }

/*    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }*/
}