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
                toggleContactInfo(v);
            }
        });
    }

    private void addViews() {
        imgArrowDown=findViewById(R.id.imgArrowDown);
    }
    public void toggleContactInfo(View view) {
        TextView txtPhone = findViewById(R.id.txtPhone);
        if (txtPhone.getVisibility() == View.GONE) {
            txtPhone.setVisibility(View.VISIBLE);
            imgArrowDown.setImageResource(R.mipmap.ic_arrow_up); // Thay đổi icon nếu cần
        } else {
            // Ẩn thông tin liên hệ nếu nó đang hiển thị
            txtPhone.setVisibility(View.GONE);
            imgArrowDown.setImageResource(R.mipmap.ic_arrow_down); // Thay đổi icon nếu cần
        }
    }

}