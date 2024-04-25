package com.thaikimhuynh.miladyapp.forgotpassword;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView txtSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        addViews();
    }

    private void addViews() {
        txtSignUp=findViewById(R.id.txtSignUp);
        txtSignUp.setPaintFlags(txtSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}
