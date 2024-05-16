package com.thaikimhuynh.miladyapp.checkout;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.AddressAdapter;
import com.thaikimhuynh.miladyapp.databinding.ActivityAddressSelectionBinding;
import com.thaikimhuynh.miladyapp.databinding.ActivityCheckOutBinding;
import com.thaikimhuynh.miladyapp.model.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerAddress;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String TAG = "AddressSelectionActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection);

        recyclerAddress = findViewById(R.id.recyclerAddress);
        recyclerAddress.setLayoutManager(new LinearLayoutManager(this));
        addressList = new ArrayList<>();
        addressAdapter = new AddressAdapter(addressList);
        recyclerAddress.setAdapter(addressAdapter);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadAddresses();

        findViewById(R.id.imgBack).setOnClickListener(v -> finish());

        findViewById(R.id.txtAddNew).setOnClickListener(v -> {
            // Navigate to AddNewAddressActivity
            startActivity(new Intent(AddressSelectionActivity.this, AddNewAddressActivity.class));
        });
    }

    private void loadAddresses() {
        String userId = getUserId();
        Log.d(TAG, "User ID: " + userId);
        if (userId != null && !userId.isEmpty()) {
            mDatabase.child("Address").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    addressList.clear(); // Clear the list to avoid duplication
                    Log.d(TAG, "DataSnapshot has children: " + dataSnapshot.hasChildren());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "Snapshot Key: " + snapshot.getKey());
                        Address address = snapshot.getValue(Address.class);
                        if (address != null) {
                            Log.d(TAG, "Address Loaded: " + address.getName() + ", " + address.getPhone() + ", " + address.getAddress());
                            addressList.add(address);
                        }
                    }
                    addressAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Address List Size: " + addressList.size());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddressSelectionActivity.this, "Failed to load addresses", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }
}

