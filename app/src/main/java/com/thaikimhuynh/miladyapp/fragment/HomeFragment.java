package com.thaikimhuynh.miladyapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.ProductHomeAdapter;
import com.thaikimhuynh.miladyapp.adapter.SliderAdapter;
import com.thaikimhuynh.miladyapp.model.ProductHomeItems;
import com.thaikimhuynh.miladyapp.model.SliderItems;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewSlider;
    private RecyclerView recyclerViewProduct;
    private SliderAdapter sliderAdapter;
    private ArrayList<SliderItems> sliderItems = new ArrayList<>();
    private List<ProductHomeItems> productList = new ArrayList<>();
    private ProductHomeAdapter productHomeAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView for sliders
        recyclerViewSlider = view.findViewById(R.id.recyclerViewSlider);
        recyclerViewSlider.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        sliderAdapter = new SliderAdapter(sliderItems);
        recyclerViewSlider.setAdapter(sliderAdapter);

        // Initialize RecyclerView for products
        recyclerViewProduct = view.findViewById(R.id.recycleViewProduct);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productHomeAdapter = new ProductHomeAdapter(getContext(), productList);
        recyclerViewProduct.setAdapter(productHomeAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Add ValueEventListener to listen for changes in the database
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
                // Notify the adapter if it's not null
                if (sliderAdapter != null) {
                    sliderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        // Load data for Product RecyclerView
        mDatabase.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("picUrl").child("0").getValue(String.class);
                    String title = snapshot.child("title").getValue(String.class);
                    Long priceLong = snapshot.child("price").getValue(Long.class);
                    // Kiểm tra null trước khi tạo đối tượng ProductHomeItems
                    if (imageUrl != null && title != null && priceLong != null) {
                        // Convert price from Long to String
                        String price = String.valueOf(priceLong);
                        ProductHomeItems product = new ProductHomeItems(imageUrl, title, price);
                        productList.add(product);
                    }
                }
                // Notify the adapter if it's not null
                if (productHomeAdapter != null) {
                    productHomeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
