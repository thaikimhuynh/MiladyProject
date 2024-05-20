package com.thaikimhuynh.miladyapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;

public class AdminDeliverySuccessfullyActivity extends AppCompatActivity {
    TextView customerName,address,totalAmount,shippingFee,discountedAmount,finalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delivery_successfully);
        customerName = findViewById(R.id.txtCusNameCheckout);
        address = findViewById(R.id.txtAddressCheckout);
        totalAmount = findViewById(R.id.txtCheckoutPrice);
        shippingFee = findViewById(R.id.txtShippingFee);
        discountedAmount = findViewById(R.id.txtVoucherCheckout);
        finalAmount = findViewById(R.id.txtTotalPriceCheckout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String customerNameText = extras.getString("customerName");
            String addressText = extras.getString("address");
            String totalAmountText = extras.getString("totalAmount");
            String shippingFeeText = extras.getString("shippingFee");
            String discountedAmountText = extras.getString("discountedAmount");
            String finalAmountText = extras.getString("finalAmount");
            customerName.setText(customerNameText);
            address.setText(addressText);
            totalAmount.setText(totalAmountText);
            shippingFee.setText(shippingFeeText);
            discountedAmount.setText(discountedAmountText);
            finalAmount.setText(finalAmountText);
        }
    }
}