package com.example.bar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity {

    ImageView googleBtn;
    ImageView facebookBtn;
    int password_Show = 0;
    TextView anonymous_login;

    //
    private FirebaseAuth Auth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = Auth.getCurrentUser();
        if (user != null) {
//            Intent intent = new Intent(getApplicationContext(), ContactsContract.Profile.class);
//            startActivity(intent);
            startActivity(new Intent(Login.this, MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //訪客登入
        anonymous_login = findViewById(R.id.anonymous_login);
        anonymous_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = Auth.getCurrentUser();
                updateUI(user);
            }
        });

        //資料設定
        Auth = FirebaseAuth.getInstance();
        FirebaseUser user = Auth.getCurrentUser();

        //判斷是否登入
        if (user != null) { //登入
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        EditText password = findViewById(R.id.etLoginPassword);
        Button loginBtn = findViewById(R.id.loginBtn);
        TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        SharedPreferences pref = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        password.setText(pref.getString("password", null));

        googleBtn = findViewById(R.id.googleBtn);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, GoogleAuthActivity.class);
                startActivity(intent);
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && accessToken.isExpired() == false) {
            startActivity(new Intent(Login.this, MainActivity.class));
        }

        facebookBtn = findViewById(R.id.facebookBtn);

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        password_Show++;
                        if (password_Show % 2 == 0) { //隱藏
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invisibility_32, 0);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else { //顯示
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_32, 0);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRegister();
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Auth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = Auth.getCurrentUser();
                                updateUI(user);
                                startActivity(new Intent(Login.this, MainActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private void authenticateUser() {
        EditText etLoginEmail = findViewById(R.id.etLoginEmail);
        EditText etLoginPassword = findViewById(R.id.etLoginPassword);

        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "請輸入電子信箱與密碼", Toast.LENGTH_SHORT).show();
            return;
        }

        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showMainActivity();
                        } else {
                            Toast.makeText(Login.this, "登入失敗",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void switchToRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }


    private void navigateToSecondActivity() {
        finish();
        startActivity(new Intent(Login.this, MainActivity.class));
    }

}