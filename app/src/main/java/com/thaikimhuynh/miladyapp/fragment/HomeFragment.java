package com.thaikimhuynh.miladyapp.fragment;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.NotificationActivity;
import com.thaikimhuynh.miladyapp.SearchActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.ProductAdapter;
import com.thaikimhuynh.miladyapp.adapter.SliderAdapter;
import com.thaikimhuynh.miladyapp.databinding.FragmentHomeBinding;
import com.thaikimhuynh.miladyapp.model.Product;
import com.thaikimhuynh.miladyapp.model.SliderItems;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    ImageView imageView29;
    private DatabaseReference mDatabase;
    private ArrayList<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private FragmentHomeBinding binding;
    private DatabaseReference mbase;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewSlider;
    private SliderAdapter sliderAdapter;
    private ArrayList<SliderItems> sliderItems = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        imageView29 = view.findViewById(R.id.imageView29);
        recyclerViewSlider = view.findViewById(R.id.recyclerViewSlider);
        recyclerViewSlider.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        sliderAdapter = new SliderAdapter(sliderItems);
        recyclerViewSlider.setAdapter(sliderAdapter);




        recyclerView = binding.recyclerProduct;
        mbase = FirebaseDatabase.getInstance().getReference("Items");


        productAdapter = new ProductAdapter(requireContext(), productList);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        SearchView searchView = view.findViewById(R.id.searchView);
        loadProducts();
        imageView29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Nếu SearchView được nhấn, mở SearchActivity
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    startActivity(intent);
                    searchView.clearFocus();
                }
            }
        });
        return view;
    }




    @Override
    public void onStart() {
        super.onStart();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sliderItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("img").getValue(String.class);
                    if (imageUrl != null) {
                        SliderItems sliderItem = new SliderItems();
                        sliderItem.setUrl(imageUrl);
                        sliderItems.add(sliderItem);
                    }
                }
                sliderAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }


    private void loadProducts() {
        mbase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();


                // Get all products as a list
                List<Product> allProducts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    Double price = snapshot.child("price").getValue(Double.class);
                    String id = snapshot.child("category_id").getValue(String.class);
                    List<String> picUrls = (List<String>) snapshot.child("picUrl").getValue();
                    String productId = snapshot.child("id").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);


                    Product product = new Product(title, price, id, picUrls, productId, description);
                    allProducts.add(product);
                }


                // Shuffle the list to randomize products
                Collections.shuffle(allProducts);


                // Select the first 16 products
                int maxProducts = Math.min(allProducts.size(), 16);
                productList.addAll(allProducts.subList(0, maxProducts));


                productAdapter.setProductList(productList);
                productAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Error loading products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

