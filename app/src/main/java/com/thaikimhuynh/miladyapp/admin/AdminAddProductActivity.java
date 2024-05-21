package com.thaikimhuynh.miladyapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thaikimhuynh.miladyapp.databinding.ActivityAdminAddProductBinding;
import com.thaikimhuynh.miladyapp.model.AdminAddProduct;

import java.util.HashMap;
import java.util.Map;

public class AdminAddProductActivity extends AppCompatActivity {
    private ActivityAdminAddProductBinding activityAddProductBinding;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddProductBinding=ActivityAdminAddProductBinding.inflate(getLayoutInflater());
        setContentView(activityAddProductBinding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addEvents();
    }

    private void addEvents() {
        activityAddProductBinding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToFirebase();
            }
        });
    }

    private void addProductToFirebase() {
        String productId = activityAddProductBinding.edtProductId.getText().toString();
        String productName = activityAddProductBinding.edtProductName.getText().toString();
        float price = Float.parseFloat(activityAddProductBinding.edtPrice.getText().toString());
        int stock = Integer.parseInt(activityAddProductBinding.edtStock.getText().toString());
        String description = activityAddProductBinding.edtDes.getText().toString();
        String imageLink = activityAddProductBinding.edtImgLink.getText().toString();
        String categoryId = activityAddProductBinding.edtCateId.getText().toString();

        // Tạo một đối tượng Map để lưu trữ thông tin của sản phẩm
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", productId);
        productMap.put("title", productName);
        productMap.put("price", price);
        productMap.put("stock", stock);
        productMap.put("description", description);
        productMap.put("picUrl", imageLink);
        productMap.put("category_id", categoryId);

        // Lấy tham chiếu đến nút "Items" trong cơ sở dữ liệu Firebase
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("Items");

        // Thêm sản phẩm mới vào Firebase Realtime Database
        itemsRef.push().setValue(productMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AdminAddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                        clearEditTexts();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminAddProductActivity.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void clearEditTexts() {
        activityAddProductBinding.edtProductId.setText("");
        activityAddProductBinding.edtProductName.setText("");
        activityAddProductBinding.edtPrice.setText("");
        activityAddProductBinding.edtStock.setText("");
        activityAddProductBinding.edtDes.setText("");
        activityAddProductBinding.edtImgLink.setText("");
        activityAddProductBinding.edtCateId.setText("");
        activityAddProductBinding.edtProductId.requestFocus();
    }


}