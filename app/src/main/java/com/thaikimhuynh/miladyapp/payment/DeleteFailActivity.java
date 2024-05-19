package com.thaikimhuynh.miladyapp.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.thaikimhuynh.miladyapp.CartPaymentMethodActivity;
import com.thaikimhuynh.miladyapp.MyWalletActivity;
import com.thaikimhuynh.miladyapp.R;

public class DeleteFailActivity extends AppCompatActivity {
    Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_fail);
        addViews();
        addEvents();
    }

    private void addEvents() {
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteFailActivity.this, MyWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addViews() {
        btn_delete = findViewById(R.id.btn_Delete);
    }


}