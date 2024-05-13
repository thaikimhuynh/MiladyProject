package com.thaikimhuynh.miladyapp.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.ProductDetailsSliderAdapter;
import com.thaikimhuynh.miladyapp.adapter.SizeAdapter;
import com.thaikimhuynh.miladyapp.databinding.ActivityProductDetailBinding;
import com.thaikimhuynh.miladyapp.helpers.ManagementCart;
import com.thaikimhuynh.miladyapp.login.LoginActivity;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductDetailActivity extends AppCompatActivity {
    private Product product;
    private String selectedSize = "";
    private ActivityProductDetailBinding productDetailBinding;
    private int quantity = 1;
    private int itemId = generateRandomItemId();

    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(productDetailBinding.getRoot());
        managementCart = new ManagementCart(this);
        loadProductDetail();
        addEvents();
        loadCateName();
        initSize();
        editQuantity();
        setupListeners();
        getIntentExtra();




    }


    private void editQuantity() {
        productDetailBinding.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) { // Đảm bảo số lượng không nhỏ hơn 1
                    quantity--;
                    productDetailBinding.txtQuantity.setText(String.valueOf(quantity));
                }
            }
        });

        productDetailBinding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                productDetailBinding.txtQuantity.setText(String.valueOf(quantity));
            }
        });
    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("36");
        list.add("37");
        list.add("38");
        list.add("39");
        list.add("40");

        SizeAdapter sizeAdapter = new SizeAdapter(list);
        sizeAdapter.setOnItemClickListener(new SizeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String size) {
                selectedSize = size;
            }
        });

        productDetailBinding.listSize.setAdapter(sizeAdapter);
        productDetailBinding.listSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadCateName() {
        Product product = (Product) getIntent().getSerializableExtra("product");
        String categoryId = product.getCategoryId();

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category").child(categoryId);
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String categoryName = dataSnapshot.child("name").getValue(String.class);

                    productDetailBinding.txtProductDetailCategory.setText(categoryName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addEvents() {
        productDetailBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadProductDetail() {
        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            String title = product.getTitle();
            String description = product.getDescription();
            double price = product.getPrice();
            List<String> picUrls = product.getPicUrls();

            productDetailBinding.txtProductDetailName.setText(title);
            productDetailBinding.txtProductDetailDescription.setText(description);
            productDetailBinding.txtProductDetailPrice.setText("$" + price);

            // Load ảnh từ picUrls vào ViewPager
            ProductDetailsSliderAdapter adapter = new ProductDetailsSliderAdapter(ProductDetailActivity.this, picUrls);
            productDetailBinding.viewPagerProductImage.setAdapter(adapter);
            productDetailBinding.circleIndicator.setViewPager(productDetailBinding.viewPagerProductImage);
        }
    }

    private void setupListeners() {
        // Add to cart button click listener
        productDetailBinding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTocart();
            }
        });
    }
    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void addTocart() {
        String userId = getUserId();
        Product product = (Product) getIntent().getSerializableExtra("product");
            if (product != null && product.getTitle() != null) {
                product.setNumberInCart(quantity);
                product.setProductSize(selectedSize);
                product.setGetItemId(String.valueOf(itemId));

                managementCart.insertProduct(product, userId,itemId);
                Toast.makeText(ProductDetailActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProductDetailActivity.this, "Product not available", Toast.LENGTH_SHORT).show();
            }
        }

    private void getIntentExtra() {
        product= (Product) getIntent().getSerializableExtra("product");

    }
    private int generateRandomItemId() {

        return new Random().nextInt(Integer.MAX_VALUE) + 1;
    }



}
