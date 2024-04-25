package com.thaikimhuynh.miladyapp.forgotpassword;


import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thaikimhuynh.miladyapp.R;

public class VerifyCodePasswordActivity extends AppCompatActivity {
    TextView txtSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        addViews();
    }

    private void addViews() {
        txtSignUp=findViewById(R.id.txtSignUp);
        txtSignUp.setPaintFlags(txtSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
};
