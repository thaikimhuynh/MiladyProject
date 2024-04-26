package com.thaikimhuynh.miladyapp.login;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.admin.AdminConfirmOrderActivity;
import com.thaikimhuynh.miladyapp.signup.SignUpActivity;

public class WelcomeActivity extends AppCompatActivity {
    Button btnHadAccount, btnStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnHadAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        });
        btnStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addViews() {
        btnHadAccount=findViewById(R.id.btnHadAccount);
        btnStarted=findViewById(R.id.btnStarted);
    }
}
