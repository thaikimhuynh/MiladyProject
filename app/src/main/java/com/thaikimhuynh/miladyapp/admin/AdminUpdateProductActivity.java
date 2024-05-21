package com.thaikimhuynh.miladyapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thaikimhuynh.miladyapp.R;

import java.util.HashMap;

public class AdminUpdateProductActivity extends AppCompatActivity {
    private EditText edtIdProduct, edtNameProduct, edtPrice, edtStock, edtDescription, edtImgLink;
    private Button btnUpdate;
    private DatabaseReference databaseReference;
    private String itemId, id, name, picUrl, description;
    private double price;
    private int stock;
    private ImageView leftArrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_product);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        // Initialize EditText fields
        edtIdProduct = findViewById(R.id.edt_id_product);
        edtNameProduct = findViewById(R.id.edt_name_product);
        edtPrice = findViewById(R.id.edt_price);
        edtStock = findViewById(R.id.edt_stock);
        edtDescription = findViewById(R.id.edt_des);
        edtImgLink = findViewById(R.id.img_link);

        // Initialize Button
        btnUpdate = findViewById(R.id.btn_update);

        // Initialize ImageView
        leftArrowButton = findViewById(R.id.left_arrow_button);

        // Retrieve data from intent
        itemId = getIntent().getStringExtra("itemId"); // Lấy itemId từ Intent
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        price = getIntent().getDoubleExtra("price", 0.0);
        stock = getIntent().getIntExtra("stock", 0);
        picUrl = getIntent().getStringExtra("picUrl");
        description = getIntent().getStringExtra("description");

        // Set data to EditText fields
        edtIdProduct.setText(id);
        edtNameProduct.setText(name);
        edtPrice.setText(String.valueOf(price));
        edtStock.setText(String.valueOf(stock));
        edtImgLink.setText(picUrl);
        edtDescription.setText(description);

        // Set event listeners
        addEvents();

        // Set onClick listener for the button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });
    }

    private void addEvents() {
        leftArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateProduct() {
        // Retrieve the values from EditText fields
        id = edtIdProduct.getText().toString();
        name = edtNameProduct.getText().toString();
        price = Double.parseDouble(edtPrice.getText().toString());
        stock = Integer.parseInt(edtStock.getText().toString());
        description = edtDescription.getText().toString();
        picUrl = edtImgLink.getText().toString();

        // Create a HashMap to store the updated values
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("id", id);
        productMap.put("name", name);
        productMap.put("price", price);
        productMap.put("stock", stock);
        productMap.put("description", description);
        productMap.put("picUrl", picUrl);

        // Update the product in Firebase using itemId
        databaseReference.child(itemId).updateChildren(productMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(AdminUpdateProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after update
                } else {
                    Toast.makeText(AdminUpdateProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Send result back to the calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", id);
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("price", price);
        resultIntent.putExtra("stock", stock);
        resultIntent.putExtra("picUrl", picUrl);
        resultIntent.putExtra("description", description);
        setResult(Activity.RESULT_OK, resultIntent);
        finish(); // Close the activity
    }
}
