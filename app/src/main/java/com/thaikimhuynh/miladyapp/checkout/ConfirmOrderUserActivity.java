package com.thaikimhuynh.miladyapp.checkout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Order;
import com.thaikimhuynh.miladyapp.model.Product;

public class ConfirmOrderUserActivity extends AppCompatActivity {
    private TextView txtProductName, txtProductPrice, txtProductSize, txtProductQuantity;
    private ImageView imgProduct;
    private static final String TAG = "ConfirmOrderUserActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order_user);
        Log.d(TAG, "onCreate: started");
        addViews();

        int orderId = getIntent().getIntExtra("orderId", -1);
        Log.d(TAG, "onCreate: received orderId: " + orderId);
        if (orderId != -1) {
            loadOrderDetails(orderId);}else
        {
            Log.e(TAG, "onCreate: Invalid orderId received");
        }
    }

    private void loadOrderDetails(int orderId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(String.valueOf(orderId));
        Log.d(TAG, "loadOrderDetails: loading order details for orderId: " + orderId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order != null) {
                    Log.d(TAG, "onDataChange: order found: " + order.toString());
                    if (!order.getProducts().isEmpty()) {
                        Product product = order.getProducts().get(0);
                        Log.d(TAG, "onDataChange: product found: " + product.toString());

                        txtProductName.setText(product.getTitle());
                        txtProductPrice.setText("$" + product.getPrice());
                        txtProductSize.setText(product.getProductSize());
                        txtProductQuantity.setText("x" + product.getNumberInCart());

                        Glide.with(ConfirmOrderUserActivity.this)
                                .load(product.getPicUrls().get(0))
                                .into(imgProduct);
                    } else {
                        Log.e(TAG, "onDataChange: No products found in the order");
                    }
                } else {
                    Log.e(TAG, "onDataChange: Order not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: error loading order details", databaseError.toException());
            }
        });
    }


    private void addViews() {
        txtProductName = findViewById(R.id.txtProductNameCheckout);
        txtProductPrice = findViewById(R.id.txtProductPriceCheckout);
        txtProductSize = findViewById(R.id.txtSize);
        txtProductQuantity = findViewById(R.id.txtProductCheckoutQuantity);
        imgProduct = findViewById(R.id.imgProductCheckout);
    }
}