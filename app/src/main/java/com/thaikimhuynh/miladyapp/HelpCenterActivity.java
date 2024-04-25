package com.thaikimhuynh.miladyapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpCenterActivity extends AppCompatActivity {
    ImageView imgArrowDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        addViews();
        addEvents();
    }

    private void addEvents() {
        imgArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    private void addViews() {
        imgArrowDown=findViewById(R.id.imgArrowDown);
    }

    }


