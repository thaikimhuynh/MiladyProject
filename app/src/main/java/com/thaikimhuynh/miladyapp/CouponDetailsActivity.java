package com.thaikimhuynh.miladyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CouponDetailsActivity extends AppCompatActivity {
    TextView txtTitle,txtdes,txtVoucherCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);

        txtTitle= findViewById(R.id.txtTitle);
        txtdes = findViewById(R.id.txtdes);
        txtVoucherCode = findViewById((R.id.txtVoucherCode));

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String vouchercode = getIntent().getStringExtra("voucherCode");

        txtTitle.setText(title);
        txtdes.setText(description);
        txtVoucherCode.setText("Voucher Code:  "+ vouchercode);
    }
}