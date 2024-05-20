package com.thaikimhuynh.miladyapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;

public class AdminOnDeliveryActivity extends AppCompatActivity {
    TextView customerName,address,totalAmount,shippingFee,discountedAmount,finalAmount;
    Button btn_prepare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_on_delivery);
        customerName = findViewById(R.id.txtCusNameCheckout);
        address = findViewById(R.id.txtAddressCheckout);
        totalAmount = findViewById(R.id.txtCheckoutPrice);
        shippingFee = findViewById(R.id.txtShippingFee);
        discountedAmount = findViewById(R.id.txtVoucherCheckout);
        finalAmount = findViewById(R.id.txtTotalPriceCheckout);
        btn_prepare = findViewById(R.id.btn_prepare);
        btn_prepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderIdText = getIntent().getStringExtra("orderId");
                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderIdText);
                ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            snapshot.getRef().child("orderStatus").setValue("Delivery Successfully");
                            Toast.makeText(AdminOnDeliveryActivity.this, "Order status updated to Delivery Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("AdminConfirmOrder", "Failed to update order status: Order not found");
                            Toast.makeText(AdminOnDeliveryActivity.this, "Failed to update order status: Order not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("AdminConfirmOrder", "Failed to update order status: " + error.getMessage());
                        Toast.makeText(AdminOnDeliveryActivity.this, "Failed to update order status: " + error.getMessage(), Toast.LENGTH_SHORT).show();                    }
                });
            }
        });

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
