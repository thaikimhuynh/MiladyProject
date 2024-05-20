package com.thaikimhuynh.miladyapp.checkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.CheckoutAdapter;
import com.thaikimhuynh.miladyapp.adapter.ProductAdapter;
import com.thaikimhuynh.miladyapp.adapter.ProductOrderAdapter;
import com.thaikimhuynh.miladyapp.model.Order;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderUserActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    RecyclerView recycler;
    TextView txtCusName, txtPhone, txtAddress;
    TextView txtTotalAmount, txtFinalAmount, txtVoucherDiscount, txtShippingFee;
    private String orderId; 
    ImageView imgBack;
    Button btnCancelOrder;

    private static final String TAG = "ConfirmOrderUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order_user);
        Log.d(TAG, "onCreate: started");
        addViews();
        orderId = getIntent().getStringExtra("orderId");
        if (orderId == null) {
            Log.e(TAG, "orderId is null");
        } else {
            Log.d(TAG, "orderId: " + orderId);
        }
        loadOrderDetails();
        
        addEvents();
    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder();
            }
        });
    }

    private void cancelOrder() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        orderRef.child("orderStatus").setValue("Cancelled")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Order status updated to 'Cancelled'");
                        Toast.makeText(ConfirmOrderUserActivity.this, "Order cancelled", Toast.LENGTH_SHORT).show();
                        btnCancelOrder.setBackgroundResource(R.drawable.grey_button_background);
                        btnCancelOrder.setEnabled(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to update order status: " + e.getMessage());
                    }
                });
    }




    private void addViews() {
        txtCusName=findViewById(R.id.txtCusName);
        txtPhone=findViewById(R.id.txtPhone);
        txtAddress=findViewById(R.id.txtAddress);
        txtShippingFee=findViewById(R.id.txtShippingFee);
        txtTotalAmount=findViewById(R.id.txtPrice);
        txtFinalAmount=findViewById(R.id.txtFinalPrice);
        txtVoucherDiscount=findViewById(R.id.txtVoucher);
        
        recycler = findViewById(R.id.recyclerProduct);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        btnCancelOrder=findViewById(R.id.btnCancelOrder);
        imgBack=findViewById(R.id.imgBack);

    }

    private void loadOrderDetails() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    List<Product> productList = order.getProducts();

                    updateUI(order);

                    ProductOrderAdapter adapter = new ProductOrderAdapter(productList);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateUI(Order order) {
        txtCusName.setText(order.getCustomerName());
        txtPhone.setText(order.getPhone());
        txtAddress.setText(order.getAddress());

        txtTotalAmount.setText("$"+String.valueOf(order.getTotalAmount()));
        txtVoucherDiscount.setText("$"+String.valueOf(order.getDiscountedAmount()));
        txtFinalAmount.setText("$"+String.valueOf(order.getFinalAmount()));
        txtShippingFee.setText("$"+String.valueOf(order.getShippingFee()));


    }
}
