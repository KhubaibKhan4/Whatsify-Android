package com.codespacepro.whatsify.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codespacepro.whatsify.Models.Users;
import com.codespacepro.whatsify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText Username, FullName, Email, Password, CPassword;
    RadioGroup SelectGender;
    TextView SignupToLogin;
    Button Signup;
    RadioButton Male, Female, Others;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    FirebaseStorage storage;
    DatabaseReference myRef;
    String male, female, others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        Signup = (Button) findViewById(R.id.btnUpdate);


        SignupToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupCreate();
            }
        });

    }

    private void init() {

        Username = (TextInputEditText) findViewById(R.id.edit_username_update);
        FullName = (TextInputEditText) findViewById(R.id.edit_fullname_update);
        Email = (TextInputEditText) findViewById(R.id.edit_email_update);
        Password = (TextInputEditText) findViewById(R.id.edit_pass_update);
        CPassword = (TextInputEditText) findViewById(R.id.edit_cpass_update);
        //  Signup = (Button) findViewById(R.id.btnSignup);

        Male = (RadioButton) findViewById(R.id.rd_male);
        Female = (RadioButton) findViewById(R.id.rd_female);
        Others = (RadioButton) findViewById(R.id.rd_others);
        SelectGender = (RadioGroup) findViewById(R.id.radiogroup);
        SignupToLogin = (TextView) findViewById(R.id.signup_to_login_txt);


        SelectGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rd_male:
                        //male
                        Male.setChecked(true);
                        Others.setChecked(false);
                        Female.setChecked(false);
                        male = Male.getText().toString();
                        break;
                    case R.id.rd_female:
                        //female
                        Male.setChecked(false);
                        Others.setChecked(false);
                        Female.setChecked(true);

                        female = Female.getText().toString();
                        break;
                    case R.id.rd_others:
                        //others
                        Male.setChecked(false);
                        Others.setChecked(true);
                        Female.setChecked(false);
                        others = Others.getText().toString();
                        break;

                }
            }
        });


    }

    public void signupCreate() {

        String username = Username.getText().toString();
        String fullname = FullName.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String cpassword = CPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Username.setError("Username Can't be Empty");
        }

        if (TextUtils.isEmpty(fullname)) {
            FullName.setError("FullName Can't be Empty");
        }

        if (TextUtils.isEmpty(email)) {
            Email.setError("Email Can't be Empty");
        }

        if (TextUtils.isEmpty(password)) {
            Password.setError("Password Can't be Empty");
        }

        if (TextUtils.isEmpty(cpassword)) {
            CPassword.setError("Password Can't be Empty");
        }

        String Gender;
        if (Male.isChecked()) {
            Gender = Male.getText().toString();
        } else if (Female.isChecked()) {
            Gender = Female.getText().toString();
        } else {
            Gender = Others.getText().toString();
        }
        if (!password.equals(cpassword)) {
            Password.setError("Password Didn't Match.");
            CPassword.setError("Password Didn't Match.");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Users users = new Users(username, fullname, email, password, Gender);
                        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(users);
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
            });
        }
    }
}