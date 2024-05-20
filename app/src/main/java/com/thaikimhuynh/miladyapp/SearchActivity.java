package com.thaikimhuynh.miladyapp;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.FilterSizeAdapter;
import com.thaikimhuynh.miladyapp.adapter.ProductAdapter;
import com.thaikimhuynh.miladyapp.adapter.SizeAdapter;
import com.thaikimhuynh.miladyapp.databinding.ActivitySearchBinding;
import com.thaikimhuynh.miladyapp.model.Product;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String selectedSize = "";
    private ArrayList<Product> productList = new ArrayList<>();
    private ProductAdapter ProductAdapter;
    private ActivitySearchBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private float minPrice = 0;
    private float maxPrice = 500;
    DatabaseReference mbase;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = binding.recyclerProduct;
        mbase = FirebaseDatabase.getInstance().getReference("Items");
        bottomSheetDialog = new BottomSheetDialog(this);

        ProductAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(ProductAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadProducts();

    }

    private void loadProducts() {
        mbase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    double price = snapshot.child("price").getValue(Double.class);
                    String id = snapshot.child("category_id").getValue(String.class);
                    List<String> picUrls = (List<String>) snapshot.child("picUrl").getValue();
                    String productId = snapshot.child("productId").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);

                    Product product = new Product(title, price, id, picUrls, productId, description);
                    productList.add(product);
                }

                ProductAdapter.setProductList(productList);
                ProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Error loading products", Toast.LENGTH_SHORT).show();
            }
        });
    }




}

