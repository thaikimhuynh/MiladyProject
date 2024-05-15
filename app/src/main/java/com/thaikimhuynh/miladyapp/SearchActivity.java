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
import com.thaikimhuynh.miladyapp.adapter.ProductHomeAdapter;
import com.thaikimhuynh.miladyapp.adapter.SizeAdapter;
import com.thaikimhuynh.miladyapp.databinding.ActivitySearchBinding;
import com.thaikimhuynh.miladyapp.model.ProductHomeItems;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String selectedSize = "";
    private List<ProductHomeItems> productList = new ArrayList<>();
    private ProductHomeAdapter productHomeAdapter;
    private ActivitySearchBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private float minPrice = 0;
    private float maxPrice = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        bottomSheetDialog = new BottomSheetDialog(this); // Create the BottomSheetDialog here

        addEvents();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        productHomeAdapter = new ProductHomeAdapter(this, productList);
        binding.recyclerProductSearch.setAdapter(productHomeAdapter);
        binding.recyclerProductSearch.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void addEvents() {
        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
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

    private void showFilterDialog() {
        bottomSheetDialog.setContentView(R.layout.alert_filter);
        RecyclerView sizeRecyclerView = bottomSheetDialog.findViewById(R.id.RecyclerSize);
        RangeSlider priceRangeSlider = bottomSheetDialog.findViewById(R.id.sizeRangeSlider);
        Button applyFilterButton = bottomSheetDialog.findViewById(R.id.btnApply);
        Button clearFilterButton = bottomSheetDialog.findViewById(R.id.btnClear);
        ImageView closeDiaglogButon = bottomSheetDialog.findViewById(R.id.btnClose);
        ArrayList<String> sizeList = new ArrayList<>();
        sizeList.add("36");
        sizeList.add("37");
        sizeList.add("38");
        sizeList.add("39");
        sizeList.add("40");

        FilterSizeAdapter sizeAdapter = new FilterSizeAdapter(sizeList);
        sizeAdapter.setOnItemClickListener(new FilterSizeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String size) {
                selectedSize = size;
            }
        });
        sizeRecyclerView.setAdapter(sizeAdapter);
        sizeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        priceRangeSlider.setValueFrom(0);
        priceRangeSlider.setValueTo(500);
        priceRangeSlider.setValues(minPrice, maxPrice);

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Float> priceRange = priceRangeSlider.getValues();
                minPrice = priceRange.get(0);
                maxPrice = priceRange.get(1);
                applyFilters(selectedSize, minPrice, maxPrice);
                bottomSheetDialog.dismiss();
            }
        });

        clearFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSize = "";
                priceRangeSlider.setValues(0f, 500f);
            }
        });
        priceRangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> values = slider.getValues();
                List<Float> roundedValues = new ArrayList<>();
                for (Float val : values) {
                    roundedValues.add((float) Math.round(val));
                }
                slider.setValues(roundedValues);
            }
        });
        closeDiaglogButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void applyFilters(String size, float minPrice, float maxPrice) {
        List<ProductHomeItems> filteredList = new ArrayList<>();
        for (ProductHomeItems item : productList) {
            float itemPrice = Float.parseFloat(item.getPrice());
            if ((size.isEmpty() || item.getTitle().contains(size)) && itemPrice >= minPrice && itemPrice <= maxPrice) {
                filteredList.add(item);
            }
        }
        productHomeAdapter.setProductList(filteredList);
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
