package com.thaikimhuynh.miladyapp.checkout;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thaikimhuynh.miladyapp.CartPaymentMethodActivity;
import com.thaikimhuynh.miladyapp.R;

public class CheckOutActivity extends AppCompatActivity {
    Button btnContinuePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnContinuePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, CartPaymentMethodActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addViews() {
        btnContinuePayment=findViewById(R.id.btnContinuePayment);

    }

}
