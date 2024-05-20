package com.thaikimhuynh.miladyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.FeedbackAdapter;
import com.thaikimhuynh.miladyapp.adapter.StarRatingAdapter;
import com.thaikimhuynh.miladyapp.model.Feedback;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewProductFeedbackActivity extends AppCompatActivity {

    RecyclerView recyclerviewFeedback;
    DatabaseReference mFeedback, mUser;
    private List<Feedback> feedbackList;
    private FeedbackAdapter feedbackAdapter;
    private Product product;
    ImageView filterIcon;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_feedback);
        product = (Product) getIntent().getSerializableExtra("product");

        addViews();
        displayFeedbacks(); // Load all feedback items by default
        addEvents();
    }

    private void addEvents() {
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingFilterDialog();
            }
        });
    }

    private void addViews() {
        recyclerviewFeedback = findViewById(R.id.rcv_feedbackList);
        filterIcon = findViewById(R.id.icFilterFeedback);
    }

    // Method to show a dialog to select star rating filter
    private void showRatingFilterDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter_feedback, null);
        bottomSheetDialog.setContentView(dialogView);

        // Find RecyclerView in the custom layout
        RecyclerView recyclerViewOptions = dialogView.findViewById(R.id.recyclerView_rating_selection);

        // Define your list of star rating options (e.g., 1, 2, 3, 4, 5)
        List<String> options = new ArrayList<>();
        options.add("1");
        options.add("2");
        options.add("3");
        options.add("4");
        options.add("5");

        // Set custom adapter to RecyclerView
        StarRatingAdapter adapter = new StarRatingAdapter(options);
        recyclerViewOptions.setAdapter(adapter);
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Set click listener for RecyclerView items
        adapter.setOnItemClickListener(new StarRatingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int selectedRating = position + 1; // Ratings start from 1, so add 1 to 'position'
                filterFeedbackByRating(selectedRating, bottomSheetDialog); // Pass the dialog to dismiss it after applying filter
            }
        });

        // Show dialog
        bottomSheetDialog.show();

        // Find and set click listener for the close button
        ImageView closeButton = dialogView.findViewById(R.id.icClose);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss(); // Dismiss the dialog using BottomSheetDialog instance
            }
        });

        // Clear filter and apply
        Button btnClearButton = dialogView.findViewById(R.id.btnClearFeedbackButton);

        btnClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset feedback list to show all feedbacks
                feedbackAdapter.updateFeedbackList(feedbackList);
                bottomSheetDialog.dismiss(); // Dismiss the dialog after clearing the filter
            }
        });

    }

    // Method to display all feedbacks
    private void displayFeedbacks() {
        feedbackList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(feedbackList, true);
        recyclerviewFeedback.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewFeedback.setAdapter(feedbackAdapter);

        mFeedback = FirebaseDatabase.getInstance().getReference("Feedback");
        mUser = FirebaseDatabase.getInstance().getReference("User");

        mFeedback.orderByChild("productId").equalTo(product.getProductId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedbackList.clear();
                for (DataSnapshot feedbackSnapshot : snapshot.getChildren()) {
                    Feedback feedback = feedbackSnapshot.getValue(Feedback.class);
                    if (feedback != null) {
                        String user_id = feedback.getUserId();
                        mUser.orderByChild("id").equalTo(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        String username = snapshot1.child("name").getValue(String.class);
                                        feedback.setUserName(username);
                                        feedbackList.add(feedback);
                                        feedbackAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ViewProductFeedbackActivity.this, "Failed to fetch username", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProductFeedbackActivity.this, "Failed to load feedback", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to filter feedback list based on selected rating
    private void filterFeedbackByRating(int selectedRating, BottomSheetDialog bottomSheetDialog) {
        List<Feedback> filteredList = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            if (feedback.getRatingStar() == selectedRating) {
                filteredList.add(feedback);
            }
        }
        feedbackAdapter.updateFeedbackList(filteredList);
        bottomSheetDialog.dismiss(); // Dismiss the dialog after applying the filter
    }
}

