package com.thaikimhuynh.miladyapp.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thaikimhuynh.miladyapp.MyWalletActivity;
import com.thaikimhuynh.miladyapp.R;

public class SuccessfullyChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_change);
    }

    public void backtoMyWalletActivity(View view) {

        Intent intent = new Intent(SuccessfullyChangeActivity.this, MyWalletActivity.class);
        startActivity(intent);

    }
}