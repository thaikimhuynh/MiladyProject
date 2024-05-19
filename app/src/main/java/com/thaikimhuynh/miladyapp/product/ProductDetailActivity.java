package com.thaikimhuynh.miladyapp.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.ProductDetailsSliderAdapter;
import com.thaikimhuynh.miladyapp.adapter.SizeAdapter;
import com.thaikimhuynh.miladyapp.databinding.ActivityProductDetailBinding;
import com.thaikimhuynh.miladyapp.helpers.ManagementCart;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            loadProductDetail();
            loadCateName();
        } else {
            Toast.makeText(this, "Product not available", Toast.LENGTH_SHORT).show();
            finish();
        }
        readWishlistStatus();
        initSize();
        editQuantity();
        setupListeners();
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void editQuantity() {
        productDetailBinding.btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                productDetailBinding.txtQuantity.setText(String.valueOf(quantity));
            }
        });

        productDetailBinding.btnPlus.setOnClickListener(v -> {
            quantity++;
            productDetailBinding.txtQuantity.setText(String.valueOf(quantity));
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
        sizeAdapter.setOnItemClickListener(size -> selectedSize = size);

        productDetailBinding.listSize.setAdapter(sizeAdapter);
        productDetailBinding.listSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadCateName() {
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

    private void loadProductDetail() {
        String title = product.getTitle();
        String description = product.getDescription();
        double price = product.getPrice();
        List<String> picUrls = product.getPicUrls();
        productDetailBinding.txtProductDetailName.setText(title);
        productDetailBinding.txtProductDetailDescription.setText(description);
        productDetailBinding.txtProductDetailPrice.setText("$" + price);

        ProductDetailsSliderAdapter adapter = new ProductDetailsSliderAdapter(this, picUrls);
        productDetailBinding.viewPagerProductImage.setAdapter(adapter);
        productDetailBinding.circleIndicator.setViewPager(productDetailBinding.viewPagerProductImage);
    }

    private void setupListeners() {
        productDetailBinding.imgBack.setOnClickListener(v -> finish());

        productDetailBinding.btnAddToCart.setOnClickListener(v -> addToCart());
        productDetailBinding.imgFavorite.setOnClickListener(v -> addToWishlist());

    }

    //Wishlist

    private int generateRandomWishlistId() {
        return (int) (Math.random() * 90000) + 10000;
    }


    private void readWishlistStatus() {
        String userId = getUserId();
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(userId);
        userWishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean productInWishlist = false;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String storedProductID = dataSnapshot.child("productID").getValue(String.class);
                    Boolean isAddedToWishlist = dataSnapshot.child("isAddedToWishlist").getValue(Boolean.class);

                    if (storedProductID != null && storedProductID.equals(product.getProductId()) && isAddedToWishlist != null && isAddedToWishlist) {
                        productInWishlist = true;
                        break;
                    }
                }

                if (productInWishlist) {
                    productDetailBinding.imgFavorite.setImageResource(R.mipmap.ic_heart_2);
                } else {
                    productDetailBinding.imgFavorite.setImageResource(R.mipmap.ic_heart_1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addToWishlist() {
        String userId = getUserId();
        if (product != null && product.getProductId() != null) {
            DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(userId);
            userWishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean productExists = false;
                    String productKey = null;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String storedProductID = dataSnapshot.child("productID").getValue(String.class);
                        if (storedProductID != null && storedProductID.equals(product.getProductId())) {
                            productExists = true;
                            productKey = dataSnapshot.getKey();
                            break;
                        }
                    }

                    if (!productExists) {
                        int wishlistId = generateRandomWishlistId();
                        Map<String, Object> wishlistItemMap = new HashMap<>();
                        wishlistItemMap.put("productID", product.getProductId());
                        wishlistItemMap.put("userID", userId);
                        wishlistItemMap.put("isAddedToWishlist", true);
                        wishlistItemMap.put("wishlistID", wishlistId);
                        userWishlistRef.child(String.valueOf(wishlistId)).setValue(wishlistItemMap);

                        Toast.makeText(ProductDetailActivity.this, "Product added to Wishlist", Toast.LENGTH_SHORT).show();
                        productDetailBinding.imgFavorite.setImageResource(R.mipmap.ic_heart_2);
                    } else {
                        userWishlistRef.child(productKey).removeValue();

                        Toast.makeText(ProductDetailActivity.this, "Product removed from Wishlist", Toast.LENGTH_SHORT).show();
                        productDetailBinding.imgFavorite.setImageResource(R.mipmap.ic_heart_1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProductDetailActivity.this, "Failed to add to Wishlist", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Product not available", Toast.LENGTH_SHORT).show();
        }
    }


//Add to cart

    private static final int DISPLAY_ERROR_DURATION = 1000;
    private Handler handler = new Handler();

    private void addToCart() {
        String userId = getUserId();
        if (product != null && product.getProductId() != null) {
            if (selectedSize.isEmpty()) {
                productDetailBinding.txtError.setText("Please select a size");
                handler.postDelayed(() -> productDetailBinding.txtError.setText(""), DISPLAY_ERROR_DURATION);
                return;
            }

            product.setNumberInCart(quantity);
            product.setProductSize(selectedSize);
            product.setGetItemId(String.valueOf(itemId));

            managementCart.insertProduct(product, userId, itemId);
            Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Product not available", Toast.LENGTH_SHORT).show();
        }
    }

    private int generateRandomItemId() {
        return new Random().nextInt(Integer.MAX_VALUE) + 1;
    }
}
