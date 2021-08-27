package com.example.logintest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DetailActivity";

    public static Context profileact;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects

    private TextView buttonLogout;
    private TextView textivewDelete;
    ImageButton gotoprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileact = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //initializing view
        buttonLogout = (TextView) findViewById(R.id.textlogout);
        textivewDelete = (TextView) findViewById(R.id.textdelete);
        gotoprofile = (ImageButton) findViewById(R.id.gotoprofile);

        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
        gotoprofile.setOnClickListener(this);

        firebaseAuth=FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (view == gotoprofile) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        if (view == textivewDelete) {
            AlertDialog.Builder myAlertBuilder =
                    new AlertDialog.Builder(DetailActivity.this);
            // alert의 title과 Messege 세팅
            myAlertBuilder.setTitle("경고");
            myAlertBuilder.setMessage("정말 회원 탈퇴를 진행하시겠습니까?");
            // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
            myAlertBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // OK 버튼을 눌렸을 경우

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "회원 탈퇴가 완료되었습니다.");
                                        Intent intent22 = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent22);
                                    }
                                }
                            });

                }
            });
            myAlertBuilder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Cancle 버튼을 눌렸을 경우
                    Toast.makeText(getApplicationContext(), "회원 탈퇴가 취소되었습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            });
            // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
            myAlertBuilder.show();

        }
    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        ; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}




