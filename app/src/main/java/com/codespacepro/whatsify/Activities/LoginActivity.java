package com.codespacepro.whatsify.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codespacepro.whatsify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText Email, Password;
    Button Login;
    TextView LoginToSignup;

    FirebaseAuth mAuth;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        mAuth = FirebaseAuth.getInstance();
        LoginToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void init() {

        Email = (TextInputEditText) findViewById(R.id.edit_email_update);
        Password = (TextInputEditText) findViewById(R.id.edit_pass_update);
        Login = (Button) findViewById(R.id.btnlogin);
        LoginToSignup = (TextView) findViewById(R.id.login_to_signup_txt);

    }

    public void login() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Email.setError("Email Can't Be Empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Password.setError("Password Can't be Empty");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog = new Dialog(LoginActivity.this);
                    dialog.setContentView(R.layout.access_succeed_dialog);
                    dialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }, 1500);

                } else {

                    dialog = new Dialog(LoginActivity.this);
                    dialog.setContentView(R.layout.access_denied_dialog);
                    dialog.show();
                    dialog.setCancelable(true);
                    Toast.makeText(LoginActivity.this, "Access Denied..", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1000);

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
            return;
        }
    }
}