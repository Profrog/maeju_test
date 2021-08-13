package com.example.logintest;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ProfileActivity";

    public static Context profileact;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;

    private Button buttonLogout;
    private TextView textivewDelete;

    //추가
    private TextView textViewUserLevel;
    private TextView textViewHak;

    private String name="";
    private String email="";
    private String hakbun="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileact=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) findViewById(R.id.textviewDelete);

        //추가
        textViewUserLevel=(TextView)findViewById(R.id.textviewUserLevel);
        textViewHak=(TextView)findViewById(R.id.textviewHak);



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

        //textViewUserEmail의 내용을 변경해 준다.
        textViewUserEmail.setText(user.getEmail()+"으로 로그인 하였습니다.");
        textViewUserLevel.setText("안녕하세요 "+name+"님!");
        textViewHak.setText("학번은 "+hakbun+"입니다.");

        //logout button event
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == textivewDelete) {
            AlertDialog.Builder myAlertBuilder =
                    new AlertDialog.Builder(ProfileActivity.this);
            // alert의 title과 Messege 세팅
            myAlertBuilder.setTitle("경고");
            myAlertBuilder.setMessage("정말 회원 탈퇴를 진행하시겠습니까?");
            // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
            myAlertBuilder.setPositiveButton("예",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    // OK 버튼을 눌렸을 경우

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "회원 탈퇴가 완료되었습니다.");
                                    }
                                }
                            });
                    Toast.makeText(getApplicationContext(),"회원 탈퇴가 완료되었습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            });
            myAlertBuilder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Cancle 버튼을 눌렸을 경우
                    Toast.makeText(getApplicationContext(),"회원 탈퇴가 취소되었습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            });
            // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
            myAlertBuilder.show();

        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }


    public void enter(View v)
    {
        Intent intent1 = new Intent(getApplicationContext(), Showin.class);
        startActivity(intent1);
    }

    public void reserve(View v){
        Intent intent1 = new Intent(getApplicationContext(), ReserveActivity.class);
        startActivity(intent1);
    }

    //이름,학번 리턴함수

    public String returnName(){return name;}
    public String returnHakbun(){return hakbun;}

}