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
import com.thaikimhuynh.miladyapp.model.ProductHomeItems;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<ProductHomeItems> productList = new ArrayList<>();
    private ProductHomeAdapter productHomeAdapter;
    private SearchView searchView;
    private ImageView imgBack;
    private TextView txtResult, txtFeatures;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        addViews();
        addEvents();
        // Khởi tạo Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    private void addEvents() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Ánh xạ SearchView và thiết lập listener để lọc danh sách khi người dùng nhập vào
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchResultText = getString(R.string.search_result_text, query);
                txtResult.setText(searchResultText);
                txtResult.setVisibility(View.VISIBLE);
                txtFeatures.setVisibility(View.GONE);
                // Filter danh sách sản phẩm dựa trên từ khóa tìm kiếm
                filterList(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    txtFeatures.setVisibility(View.VISIBLE);
                    txtResult.setVisibility(View.GONE);
                }
                filterList(newText);
                return true;
            }
        });
    }

    private void addViews() {
        // Ánh xạ ImageView và thiết lập onClickListener để quay lại màn hình trước đó
        imgBack = findViewById(R.id.imgBack);
        searchView = findViewById(R.id.searchView);
        txtResult = findViewById(R.id.txtResult);
        txtFeatures=findViewById(R.id.txtFetures);
        // Ánh xạ RecyclerView và set LayoutManager
        RecyclerView recyclerProductSearch = findViewById(R.id.recyclerProductSearch);
        recyclerProductSearch.setLayoutManager(new GridLayoutManager(this, 2));
        // Khởi tạo Adapter
        productHomeAdapter = new ProductHomeAdapter(this, productList);
        recyclerProductSearch.setAdapter(productHomeAdapter);
    }

    // Phương thức để lọc danh sách sản phẩm dựa trên từ khóa tìm kiếm
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

        // Load dữ liệu cho RecyclerView
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
                if (productHomeAdapter != null) {
                    productHomeAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi có sự cố xảy ra với database
                Toast.makeText(SearchActivity.this, "Error loading", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
