package com.thaikimhuynh.miladyapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.MainActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.forgotpassword.ForgotPasswordActivity;
import com.thaikimhuynh.miladyapp.signup.SignUpActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText edtPhoneNumber, edtPassword;
    Button btnLogin;
    TextView txtForgotPassWord, txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPhoneNumber= findViewById(R.id.edtPhoneNumber);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        txtForgotPassWord=findViewById(R.id.txtForgotPassWord);
        txtForgotPassWord.setPaintFlags(txtForgotPassWord.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setPaintFlags(txtSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePhoneNumber() | !validatePassword()){

                }else {
                    checkPhoneNumber();
                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    public Boolean validatePhoneNumber(){
        String val= edtPhoneNumber.getText().toString();
        if(val.isEmpty()){
            edtPhoneNumber.setError("Please fill in your Phone Number");
            return  false;
        } else {
            edtPhoneNumber.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val= edtPassword.getText().toString();
        if(val.isEmpty()){
            edtPassword.setError("Please fill in Password");
            return  false;
        } else {
            edtPassword.setError(null);
            return true;
        }
    }
    public void checkPhoneNumber(){
        String UserPhoneNumber = edtPhoneNumber.getText().toString().trim();
        String UserPassWord = edtPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUserDatabase = reference.orderByChild("phoneNumber").equalTo(UserPhoneNumber);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    edtPhoneNumber.setError(null);
                    String PasswordFromDB= snapshot.child(UserPhoneNumber).child("password").getValue(String.class);
                    if (!Objects.equals(PasswordFromDB, UserPassWord)){
                        edtPhoneNumber.setError(null);
                        Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        edtPassword.setError("Wrong password or phone number!");
                        edtPassword.requestFocus();
                    }
                }else {
                    edtPhoneNumber.setError("Unregistered Phone Number");
                    edtPhoneNumber.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void openForgotPasswordActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}