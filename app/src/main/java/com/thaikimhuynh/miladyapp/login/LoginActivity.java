package com.thaikimhuynh.miladyapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.forgotpassword.ForgotPasswordActivity;
import com.thaikimhuynh.miladyapp.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {
    TextView txtForgotPassWord, txtSignUp;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();
    }



    private void addViews() {
        txtForgotPassWord=findViewById(R.id.txtForgotPassWord);
        txtForgotPassWord.setPaintFlags(txtForgotPassWord.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtSignUp=findViewById(R.id.txtSignUp);
        txtSignUp.setPaintFlags(txtSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnLogin=findViewById(R.id.btnLogin);
    }

    public void openForgotPassWordActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void openSignUpActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}