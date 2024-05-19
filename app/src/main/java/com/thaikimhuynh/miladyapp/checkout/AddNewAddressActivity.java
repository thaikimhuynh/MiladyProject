package com.thaikimhuynh.miladyapp.checkout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.databinding.ActivityAddNewAddressBinding;

import java.util.HashMap;
import java.util.Map;

public class AddNewAddressActivity extends AppCompatActivity {

    private ActivityAddNewAddressBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAddress();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveAddress() {
        String name = binding.edtName.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String address = binding.edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.edtName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            binding.edtPhone.setError("Phone Number is required");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            binding.edtAddress.setError("Address is required");
            return;
        }

        String userId = getUserId();
        if (userId != null && !userId.isEmpty()) {
            // Reference to the user's address node
            DatabaseReference userAddressRef = mDatabase.child("Address").child(userId);

            // Retrieve the highest current ID
            userAddressRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long nextId = 1; // Default to 1 if no addresses exist
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            nextId = Long.parseLong(snapshot.getKey()) + 1; // Increment the highest current ID
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    // Prepare address data
                    Map<String, Object> addressMap = new HashMap<>();
                    addressMap.put("name", name);
                    addressMap.put("phone", phone);
                    addressMap.put("address", address);

                    // Save the address with the incremented ID
                    userAddressRef.child(String.valueOf(nextId)).setValue(addressMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddNewAddressActivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddNewAddressActivity.this, "Failed to save address", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddNewAddressActivity.this, "Failed to get address ID", Toast.LENGTH_SHORT).show();
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
