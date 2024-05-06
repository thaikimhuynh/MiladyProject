package com.thaikimhuynh.miladyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.ProductAdapter;
import com.thaikimhuynh.miladyapp.model.Category;
import com.thaikimhuynh.miladyapp.model.Product;
import com.thaikimhuynh.miladyapp.product.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    TextView categoryTitle;
    RecyclerView recyclerView;
    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        addViews();
        String categoryID = getIntent().getStringExtra("categoryId");
        String category_Title = getIntent().getStringExtra("categoryName");


        categoryTitle.setText(category_Title);
        mbase = FirebaseDatabase.getInstance().getReference("Items");

        loadProducts(categoryID);








    }

    private void loadProducts(String categoryID) {
        mbase.orderByChild("category_id").equalTo(categoryID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Product> products = new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    String title = snapshot.child("title").getValue(String.class);
                    double price = snapshot.child("price").getValue(Double.class);
                    String id = snapshot.child("category_id").getValue(String.class);

                    List<String> picUrls = (List<String>) snapshot.child("picUrl").getValue();
                    String productId = snapshot.child("productId").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);


                    products.add(new Product(title, price, id, picUrls, productId, description));
                    ProductAdapter itemProductAdapter = new ProductAdapter(ProductListActivity.this, products);
                    recyclerView.setAdapter(itemProductAdapter);
                    recyclerView.setLayoutManager(
                            new GridLayoutManager(ProductListActivity.this, 2));



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addViews() {
        categoryTitle = findViewById(R.id.title_category);
        recyclerView = findViewById(R.id.rcv_productlist);
    }

    public void backtoCategory(View view) {
        finish();

    }
}