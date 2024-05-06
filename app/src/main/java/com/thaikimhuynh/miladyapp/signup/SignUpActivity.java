package com.thaikimhuynh.miladyapp.signup;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.login.LoginActivity;

public class SignUpActivity extends AppCompatActivity {
    EditText edtPhoneNumber,edtEmail,edtPassword;
    TextView txtLogin;
    Button btnSignUp;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtPhoneNumber= findViewById(R.id.edtPhoneNumber);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        txtLogin= findViewById(R.id.txtLogin);
        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database= FirebaseDatabase.getInstance();
                reference= database.getReference("User");

                String PhoneNumber = edtPhoneNumber.getText().toString();
                String Email = edtEmail.getText().toString();
                String Password = edtPassword.getText().toString();

                HelperClass helperClass= new HelperClass(PhoneNumber, Email, Password);
                reference.child(PhoneNumber).setValue(helperClass);
                Toast.makeText(SignUpActivity.this,"Signup successfully",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
