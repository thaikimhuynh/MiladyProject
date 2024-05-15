package com.thaikimhuynh.miladyapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {
    RatingBar ratingBar;
    EditText edt_feedback;
    Button submitButtonFeedBack;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        addViews();

        submitButtonFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = getUserId();
                String rating = String.valueOf(ratingBar.getRating());
                String feedBackContent = edt_feedback.getText().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference("PointWallet");

                // Generating a unique key using push()
                String uniqueKey = mDatabase.push().getKey();

                // Storing feedback data with the generated unique key
                mDatabase.child(uniqueKey).child("userID").setValue(userId);
                mDatabase.child(uniqueKey).child("ratingStar").setValue(rating);
                mDatabase.child(uniqueKey).child("feedBackContent").setValue(feedBackContent);

                // Optionally, you can also store other data like ProductID if needed.
            }
        });
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void addViews() {
        ratingBar = findViewById(R.id.ratingBar);
        edt_feedback = findViewById(R.id.edt_feedback);
        submitButtonFeedBack = findViewById(R.id.submitButtonFeedBack);
    }
}
