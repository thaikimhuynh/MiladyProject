package com.thaikimhuynh.miladyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

        // Disable the submit button initially
        submitButtonFeedBack.setEnabled(false);

        // Add text and rating change listeners
        edt_feedback.addTextChangedListener(textWatcher);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                checkInput();
            }
        });

        submitButtonFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = (int) ratingBar.getRating();
                feedBackContent = edt_feedback.getText().toString().trim();
                if (rating > 0 && !feedBackContent.isEmpty()) {
                    updateUserPoints(200);

                    // Ensure feedback is created and stored only when ID is unique
                    generateUniqueId(new UniqueIdCallback() {
                        @Override
                        public void onUniqueIdFound(int uniqueId) {
                            saveFeedback(uniqueId);
                            Intent intent = new Intent(FeedbackActivity.this, SubmitFeedBackActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    if (rating == 0) {
                        Toast.makeText(FeedbackActivity.this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
                    }
                    if (feedBackContent.isEmpty()) {
                        Toast.makeText(FeedbackActivity.this, "Please provide feedback.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkInput() {
        String feedbackText = edt_feedback.getText().toString().trim();
        int ratingValue = (int) ratingBar.getRating();
        submitButtonFeedBack.setEnabled(!feedbackText.isEmpty() && ratingValue > 0);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInput();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

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
