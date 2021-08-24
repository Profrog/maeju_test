package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.logintest.MainActivity;
import com.example.logintest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private Button sign_up;

    //로그아웃 기능 구현 위함
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        sign_up = findViewById(R.id.signUpButton);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    // When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void signUp() {
        // 이메일
        EditText emailEditText = findViewById(R.id.emailEditText);
        String email = emailEditText.getText().toString();
        // 비밀번호
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        String password = passwordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.signOut();


                            firebaseAuth.signInWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            System.out.print(task.isSuccessful());
                                            if(task.isSuccessful()) {
                                                Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }
                                    });


//                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            Toast.makeText(SignUpActivity.this, "등록 완료", Toast.LENGTH_SHORT).show();
//                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }

    Intent intent = getIntent();
    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }

}