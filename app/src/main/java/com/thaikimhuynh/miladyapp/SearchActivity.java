package com.thaikimhuynh.miladyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.ProductHomeAdapter;
import com.thaikimhuynh.miladyapp.databinding.ActivitySearchBinding;
import com.thaikimhuynh.miladyapp.model.ProductHomeItems;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<ProductHomeItems> productList = new ArrayList<>();
    private ProductHomeAdapter productHomeAdapter;
    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addEvents();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        productHomeAdapter = new ProductHomeAdapter(this, productList);
        binding.recyclerProductSearch.setAdapter(productHomeAdapter);
        binding.recyclerProductSearch.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void addEvents() {
        binding.imgBack.setOnClickListener(v -> onBackPressed());

        binding.searchView.requestFocus();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchResultText = getString(R.string.search_result_text, query);
                binding.txtResult.setText(searchResultText);
                binding.txtResult.setVisibility(View.VISIBLE);
                binding.txtFetures.setVisibility(View.GONE);
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    binding.txtFetures.setVisibility(View.VISIBLE);
                    binding.txtResult.setVisibility(View.GONE);
                }
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<ProductHomeItems> filteredList = new ArrayList<>();
        for (ProductHomeItems item : productList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        productHomeAdapter.setProductList(filteredList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("picUrl").child("0").getValue(String.class);
                    String title = snapshot.child("title").getValue(String.class);
                    Long priceLong = snapshot.child("price").getValue(Long.class);
                    if (imageUrl != null && title != null && priceLong != null) {
                        String price = String.valueOf(priceLong);
                        ProductHomeItems product = new ProductHomeItems(imageUrl, title, price);
                        productList.add(product);
                    }
                }
                productHomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Error loading", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
