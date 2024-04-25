package com.thaikimhuynh.miladyapp.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;

public class VerifyAccountActivity extends AppCompatActivity {
    TextView txtLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        addViews();
    }

    private void addViews() {
        txtLogin=findViewById(R.id.txtLogin);
        txtLogin.setPaintFlags(txtLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }
};
