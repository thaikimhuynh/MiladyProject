package com.thaikimhuynh.miladyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_feedback);
        product = (Product) getIntent().getSerializableExtra("product");

        addViews();
    }

    private void addViews() {
        recyclerviewFeedback = findViewById(R.id.rcv_feedbackList);
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
                        feedbackList.add(feedback);
                        String user_id = feedback.getUserId();
                        fetchUsernameForFeedback(user_id);
                    }
                }
                feedbackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProductFeedbackActivity.this, "Failed to load feedback", Toast.LENGTH_SHORT).show();
            }
        });

        // Find and set click listener for filter icon
        ImageView filterIcon = findViewById(R.id.icFilterFeedback);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingFilterDialog();
            }
        });
    }

    // Method to show a dialog to select star rating filter
    private void showRatingFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter_feedback, null);
        builder.setView(dialogView);

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
        // Set layout manager to horizontal orientation
        recyclerViewOptions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // Set click listener for RecyclerView items
        adapter.setOnItemClickListener(new StarRatingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle filter selection
                int selectedRating = position + 1; // Ratings start from 1, so add 1 to 'position'
                filterFeedbackByRating(selectedRating);
            }
        });

        // Show dialog
        builder.show();
    }

    // Method to filter feedback list based on selected rating
    private void filterFeedbackByRating(int selectedRating) {
        List<Feedback> filteredList = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            if (feedback.getRatingStar() == selectedRating) {
                filteredList.add(feedback);
            }
        }
        // Update adapter with filtered list
        feedbackAdapter.updateFeedbackList(filteredList);
    }

    private void fetchUsernameForFeedback(String userId) {
        mUser.orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                        String username = snapshot1.child("name").getValue(String.class);
                        // Update the feedback object with the username
                        for (Feedback feedback : feedbackList) {
                            if (feedback.getUserId().equals(userId)) {
                                feedback.setUserName(username);
                                // Notify adapter of data change
                                feedbackAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
            }
        });
    }
}
