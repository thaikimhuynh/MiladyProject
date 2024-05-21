package com.thaikimhuynh.miladyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SubmitFeedBackActivity extends AppCompatActivity {
    Button viewMyPointsButton, backtoHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_feed_back);
        addViews();
        addEvents();

    }

    private void addEvents() {
        viewMyPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubmitFeedBackActivity.this, EarnPointActivity.class);
                startActivity(intent);
            }
        });
        backtoHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubmitFeedBackActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addViews() {
        viewMyPointsButton = findViewById(R.id.viewMyPointsButton);
        backtoHomeButton = findViewById(R.id.backtoHomeButton);
    }
}