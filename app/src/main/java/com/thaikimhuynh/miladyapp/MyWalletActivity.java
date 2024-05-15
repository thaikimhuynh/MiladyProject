package com.thaikimhuynh.miladyapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.ItemAdapter;
import com.thaikimhuynh.miladyapp.model.PaymentGroup;
import com.thaikimhuynh.miladyapp.model.PaymentItem;

import java.util.ArrayList;
import java.util.List;

public class MyWalletActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PaymentGroup> mList;
    private ItemAdapter itemAdapter;
    DatabaseReference mbase_1, mbase_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        addViews();
        String userId = getUserId();

        mbase_2 = FirebaseDatabase.getInstance().getReference("PaymentMethod");
        mbase_1 = FirebaseDatabase.getInstance().getReference("PaymentAccount");


        loadPaymentMethod(userId);

    }



    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void loadPaymentMethod(String userId) {
        mList = new ArrayList<>();

        mbase_1.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String paymentMethodId = snapshot.child("paymentMethodId").getValue(String.class);

                    mbase_2.orderByChild("id").equalTo(paymentMethodId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot paymentMethodSnapshot) {
                            for (DataSnapshot methodSnapshot : paymentMethodSnapshot.getChildren()) {
                                String eWalletName = methodSnapshot.child("type").getValue(String.class);
                                String imgLogo = methodSnapshot.child("logo").getValue(String.class);
                                Log.d("imgLogo", "imgLogo: " + imgLogo);

                                String userName = snapshot.child("name").getValue(String.class);
                                String userAccountNumber = snapshot.child("accountNumber").getValue(String.class);
                                String layout = eWalletName.equals("Momo") || eWalletName.equals("Zalo Pay")? "1" : "2";

                                PaymentItem paymentItem = new PaymentItem(eWalletName, userName, userAccountNumber, imgLogo, layout);
                                addItemToGroup(paymentItem);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError paymentMethodError) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


    private void addItemToGroup(PaymentItem paymentItem) {
        boolean foundGroup = false;

        for (PaymentGroup group : mList) {
            // Check if the group name matches the payment method type
            if ((isEwallet(paymentItem) && group.getItemText().equalsIgnoreCase("E-wallet")) ||
                    (!isEwallet(paymentItem) && group.getItemText().equalsIgnoreCase("Banking"))) {
                group.getItemList().add(paymentItem);
                foundGroup = true;
                break; // Break after adding the item to the group
            }
        }

        if (!foundGroup) {
            // Create a new group
            List<PaymentItem> items = new ArrayList<>();
            items.add(paymentItem);
            String groupName = isEwallet(paymentItem) ? "E-wallet" : "Banking";
            mList.add(new PaymentGroup(items, groupName));
        }
        Log.d("mList", "Loaded items: " + mList);

        // Update the RecyclerView adapter
        if (itemAdapter == null) {
            itemAdapter = new ItemAdapter(MyWalletActivity.this, mList);
            recyclerView.setAdapter(itemAdapter);
            itemAdapter.notifyDataSetChanged();

        } else {
            itemAdapter.notifyDataSetChanged();
        }
    }



    private boolean isEwallet(PaymentItem paymentItem) {
        String paymentMethodName = paymentItem.getWallet_name().toLowerCase();
        return paymentMethodName.equals("momo") || paymentMethodName.equals("zalo pay");
    }

    private void addViews() {
        recyclerView = findViewById(R.id.rcv_mywallet);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
