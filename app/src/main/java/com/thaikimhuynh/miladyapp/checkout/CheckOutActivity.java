package com.thaikimhuynh.miladyapp.checkout;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thaikimhuynh.miladyapp.CartPaymentMethodActivity;
import com.thaikimhuynh.miladyapp.R;

public class CheckOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
    }

    public void openPaymentMethod(View view) {
        Intent intent = new Intent(CheckOutActivity.this, CartPaymentMethodActivity.class);
        startActivity(intent);
    }
}
