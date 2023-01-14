package com.example.bar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    int password_Show = 0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            return;
        }

        EditText fullname = findViewById(R.id.et_fullname);
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        EditText conPassword = findViewById(R.id.et_conPassword);

        Button registerBtn = findViewById(R.id.registerBtn);
        TextView loginNowBtn = findViewById(R.id.loginNow);

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
        conPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (conPassword.getRight() - conPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        password_Show++;
                        if (password_Show % 2 == 0) { //隱藏
                            conPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invisibility_32, 0);
                            conPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else { //顯示
                            conPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_32, 0);
                            conPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLogin();
            }
        });
    }

    private void registerUser() {
        EditText etfullname = findViewById(R.id.et_fullname);
        EditText etemail = findViewById(R.id.et_email);
        EditText etpassword = findViewById(R.id.et_password);
        EditText etconPassword = findViewById(R.id.et_conPassword);

        String fullname = etfullname.getText().toString();
        String email = etemail.getText().toString();
        String password = etpassword.getText().toString();
        String conPassword = etconPassword.getText().toString();

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Register.this, "請填寫所有資料", Toast.LENGTH_SHORT).show();
        } else if (!matcher.matches()) {
            Toast.makeText(Register.this, "電子郵件格式錯誤", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(conPassword)) {
            Toast.makeText(Register.this, "密碼不一致", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(Register.this, "密碼請大於六位數", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                com.example.bar.User user = new User(fullname, email);
                                FirebaseDatabase.getInstance().getReference("users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                showMainActivity();
                                            }
                                        });
                            } else {
                                Toast.makeText(Register.this, "電子信箱已被註冊",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}