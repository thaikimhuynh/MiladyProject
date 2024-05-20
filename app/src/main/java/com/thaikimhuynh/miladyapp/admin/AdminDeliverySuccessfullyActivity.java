package com.thaikimhuynh.miladyapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.ProductOrderAdapter;
import com.thaikimhuynh.miladyapp.model.Order;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.List;

public class AdminDeliverySuccessfullyActivity extends AppCompatActivity {
    TextView customerName,address,totalAmount,shippingFee,discountedAmount,finalAmount;
    RecyclerView recycler;
    private String orderId;


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
        recycler = findViewById(R.id.recyclerCheckout);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        orderId = getIntent().getStringExtra("orderId");
        loadOrderDetails();

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

    private void loadOrderDetails() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    List<Product> productList = order.getProducts();
                    ProductOrderAdapter adapter = new ProductOrderAdapter(productList);
                    recycler.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    }
