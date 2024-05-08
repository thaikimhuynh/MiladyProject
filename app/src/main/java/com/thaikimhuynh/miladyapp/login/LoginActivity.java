package com.thaikimhuynh.miladyapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;

public class LoginActivity extends AppCompatActivity {
    TextView txtForgotPassWord, txtSignUp;
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

    }

}