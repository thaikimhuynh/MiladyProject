package com.thaikimhuynh.miladyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.ItemAdapter;
import com.thaikimhuynh.miladyapp.adapter.ProductAdapter;
import com.thaikimhuynh.miladyapp.model.PaymentGroup;
import com.thaikimhuynh.miladyapp.model.PaymentItem;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MyWalletActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PaymentGroup> mList;
    private List<PaymentItem> mListItem;
    private ItemAdapter itemAdapter;
    DatabaseReference mbase_1, mbase_2, mbase_user;
    String layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        addViews();
        String userId = getIntent().getStringExtra("userId_eWall");
        mbase_2 = FirebaseDatabase.getInstance().getReference("PaymentMethod");
        mbase_1 = FirebaseDatabase.getInstance().getReference("PaymentAccount");
        mbase_user = FirebaseDatabase.getInstance().getReference("User");

        loadPaymentMethod(userId);

    }

    private void loadPaymentMethod(String userId) {
        mbase_1.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                mListItem = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String paymentMethodId = snapshot.child("paymentMethodId").getValue(String.class);

                    // Retrieve additional details from PaymentMethod table
                    mbase_2.orderByChild("id").equalTo(paymentMethodId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot paymentMethodSnapshot) {
                            String eWalletName = paymentMethodSnapshot.child("name").getValue(String.class);
                            String imgLogo = paymentMethodSnapshot.child("logo").getValue(String.class);

                            // Retrieve name and phone number from User table
                            mbase_user.orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    String userName = userSnapshot.child("userName").getValue(String.class);
                                    String userPhoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);
                                    if(eWalletName.equals("Momo")) {
                                        layout = "1";
                                    } else {
                                        layout = "2";
                                    }
                                    mListItem.add(new PaymentItem(eWalletName, userName, userPhoneNumber, imgLogo, layout));
                                    mList.add(new PaymentGroup(mListItem, eWalletName));

                                    if (mList.size() > 0) {
                                        itemAdapter = new ItemAdapter(MyWalletActivity.this, mList);
                                        recyclerView.setAdapter(itemAdapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(MyWalletActivity.this));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError userError) {
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError paymentMethodError) {
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


    private void addViews() {
        recyclerView = findViewById(R.id.rcv_mywallet);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}