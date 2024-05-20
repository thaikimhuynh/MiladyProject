package com.thaikimhuynh.miladyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Feedback;
import com.thaikimhuynh.miladyapp.model.Order;
import com.thaikimhuynh.miladyapp.model.PointWallet;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.List;
import java.util.Random;

public class FeedbackActivity extends AppCompatActivity {
    RatingBar ratingBar;
    EditText edt_feedback;
    Button submitButtonFeedBack;
    DatabaseReference mDatabase, mWalletPoint;
    String userId;
    List<Product> products;
    int rating;
    String feedBackContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        addViews();
        mDatabase = FirebaseDatabase.getInstance().getReference("Feedback");
        mWalletPoint = FirebaseDatabase.getInstance().getReference("PointWallet");
        getOrderInfo();

        submitButtonFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = (int) ratingBar.getRating();
                feedBackContent = edt_feedback.getText().toString();
                updateUserPoints(200);


                // Ensure feedback is created and stored only when ID is unique
                generateUniqueId(new UniqueIdCallback() {
                    @Override
                    public void onUniqueIdFound(int uniqueId) {
                        saveFeedback(uniqueId);
                    }
                });
                Intent intent = new Intent(FeedbackActivity.this, SubmitFeedBackActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUserPoints(int points) {
        mWalletPoint.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Point wallet exists for the user
                    for (DataSnapshot pointWalletSnapshot : snapshot.getChildren()) {
                        PointWallet pointWallet = pointWalletSnapshot.getValue(PointWallet.class);
                        if (pointWallet != null) {
                            int newTotalPoints = pointWallet.getTotalPoint() + points;
                            pointWallet.setTotalPoint(newTotalPoints);
                            mWalletPoint.child(pointWalletSnapshot.getKey()).setValue(pointWallet);
                        }
                    }
                } else {
                    // Point wallet doesn't exist for the user, generate a new one
                    generateUniqueWalletId(points);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void generateUniqueWalletId(int points) {
        Random random = new Random();
        int walletId = 1000 + random.nextInt(9000);

        mWalletPoint.child(String.valueOf(walletId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // ID already exists, generate a new one
                    generateUniqueWalletId(points);
                } else {
                    // ID is unique, create the point wallet
                    PointWallet pointWallet = new PointWallet();
                    pointWallet.setUserId(userId);
                    pointWallet.setTotalPoint(points);
                    pointWallet.setWalletId(walletId);
                    mWalletPoint.child(String.valueOf(walletId)).setValue(pointWallet);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }



    private void getOrderInfo() {
        Order order = (Order) getIntent().getSerializableExtra("order");
        userId = order.getUserId();
        products = order.getProducts();
    }

    private void saveFeedback(int uniqueId) {
        Feedback feedback = new Feedback();
        for (Product product : products) {
            String productId = product.getProductId();
            feedback.setUserId(userId);
            feedback.setProductId(productId);
            feedback.setRatingStar(rating);
            feedback.setFeedbackContent(feedBackContent);
            feedback.setFeedbackId(uniqueId);

            mDatabase.child(String.valueOf(uniqueId)).setValue(feedback);
        }
    }

    private void generateUniqueId(UniqueIdCallback callback) {
        Random random = new Random();
        int feedbackId = 1000 + random.nextInt(9000);

        mDatabase.child(String.valueOf(feedbackId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // ID already exists, generate a new one
                    generateUniqueId(callback);
                } else {
                    // ID is unique
                    callback.onUniqueIdFound(feedbackId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void addViews() {
        ratingBar = findViewById(R.id.ratingBar);
        edt_feedback = findViewById(R.id.edt_feedback);
        submitButtonFeedBack = findViewById(R.id.submitButtonFeedBack);
    }

    interface UniqueIdCallback {
        void onUniqueIdFound(int uniqueId);
    }
}
