package com.thaikimhuynh.miladyapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.SliderAdapter;
import com.thaikimhuynh.miladyapp.domain.SliderItems;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewSlider;
    private SliderAdapter sliderAdapter;
    private ArrayList<SliderItems> SliderItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Banner");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewSlider = view.findViewById(R.id.recyclerViewSlider);
        recyclerViewSlider.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        sliderAdapter = new SliderAdapter(SliderItems);
        recyclerViewSlider.setAdapter(sliderAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Add ValueEventListener to listen for changes in the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SliderItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("img").getValue(String.class);
                    if (imageUrl != null) {
                        SliderItems sliderItem = new SliderItems();
                        sliderItem.setUrl(imageUrl);
                        SliderItems.add(sliderItem);
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
}
