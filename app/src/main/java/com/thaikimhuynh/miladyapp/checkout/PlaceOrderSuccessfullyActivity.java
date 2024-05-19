package com.thaikimhuynh.miladyapp.checkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Order;

public class PlaceOrderSuccessfullyActivity extends AppCompatActivity {
    TextView txtOrderNumber, txtDate, txtName, txtAddress, txtTotal, txtPaymentMethod;
    private DatabaseReference ordersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_successfully);

        addViews();
       Order order = (Order) getIntent().getSerializableExtra("order");

        if (order != null) {
            loadOrderDetails(order);
        } else {
            Toast.makeText(this, "No Order ID found", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadOrderDetails(Order order) {

        // Populate the UI with order details
        txtOrderNumber.setText(String.valueOf(order.getOrderId()));
        txtDate.setText(order.getOrderDate());
        txtName.setText(order.getCustomerName());
        txtAddress.setText(order.getAddress());
        txtTotal.setText(String.format("$%.2f", order.getFinalAmount()));
        txtPaymentMethod.setText(order.getPaymentMethod());
    //                       txtPaymentMethod
                    }






    private void addViews() {
        txtOrderNumber=findViewById(R.id.txtOrderNumber);
        txtDate=findViewById(R.id.txtDate);
        txtName=findViewById(R.id.txtName);
        txtAddress=findViewById(R.id.txtAddress);
        txtTotal=findViewById(R.id.txtTotal);
        txtPaymentMethod=findViewById(R.id.txtPaymentMethod);


    }
}