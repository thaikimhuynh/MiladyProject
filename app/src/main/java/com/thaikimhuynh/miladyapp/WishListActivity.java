package com.thaikimhuynh.miladyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.WishlistAdapter;
import com.thaikimhuynh.miladyapp.model.Product;
import com.thaikimhuynh.miladyapp.product.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class WishListActivity extends AppCompatActivity implements WishlistAdapter.OnDeleteItemClickListener {
    RecyclerView recyclerView;
    ImageView imgBack;
    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        addViews();
        mbase = FirebaseDatabase.getInstance().getReference("Wishlist");
        loadWishlist();
        backtoProfile();
    }

    private void backtoProfile() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void loadWishlist() {
        String userId = getUserId();
        mbase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> productIDs = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String productID = snapshot.child("productID").getValue(String.class);
                    Log.d("WishlistActivity", "Product ID: " + productID);
                    productIDs.add(productID);

                }

                loadProductDetails(productIDs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WishListActivity.this, "Failed to load wishlist: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductDetails(ArrayList<String> productIDs) {
        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("Items");
        ArrayList<Product> products = new ArrayList<>();

        for (String productID : productIDs) {
            itemRef.orderByChild("id").equalTo(productID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        String id = productSnapshot.child("id").getValue(String.class);
                        String title = productSnapshot.child("title").getValue(String.class);
                        double price = productSnapshot.child("price").getValue(Double.class);
                        String category_id = productSnapshot.child("category_id").getValue(String.class);
                        String description = productSnapshot.child("description").getValue(String.class);
                        List<String> picUrls = (List<String>) productSnapshot.child("picUrl").getValue();

                        Product product = new Product();
                        product.setTitle(title);
                        product.setProductId(id);
                        product.setCategoryId(category_id);
                        product.setPrice(price);
                        product.setDescription(description);
                        product.setPicUrls(picUrls);

                        Log.d("ProductInfo", "Pro ID: " + productID + ", Title: " + title);
                        products.add(product);
                    }

                    WishlistAdapter itemWishlistAdapter = new WishlistAdapter(WishListActivity.this, products);
                    itemWishlistAdapter.setOnDeleteItemClickListener(WishListActivity.this);
                    recyclerView.setAdapter(itemWishlistAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(WishListActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(WishListActivity.this, "Failed to load product details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void addViews() {
        recyclerView = findViewById(R.id.rc_wishlist);
        imgBack=findViewById(R.id.btnBacktoProfile);
    }

    @Override
    public void onDeleteItemClick(Product product) {
        deleteProductFromWishlist(product);
    }

    private void deleteProductFromWishlist(Product product) {
        String userId = getUserId();
        mbase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String product_id = snapshot.child("productID").getValue(String.class);
                    if (product_id != null && product_id.equals(product.getProductId())) {
                        snapshot.getRef().removeValue();
                        break;
                    }
                }
                loadWishlist();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WishListActivity.this, "Failed to delete product from wishlist: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
