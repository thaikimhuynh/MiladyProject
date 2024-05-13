package com.thaikimhuynh.miladyapp.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.thaikimhuynh.miladyapp.R;

public class AddNewWalletActivity extends AppCompatActivity {
    TextView page_title, ewallet_name, phoneNumber;
    EditText edt_username, edt_phoneNumber;
    Spinner spinnerEWalletName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_wallet);
        addViews();
        String layout_set = getIntent().getStringExtra("layout");
        if (layout_set.equals("2")){
            resetText();
            populateSpinnerForLayout2();
        } else {
            populateSpinnerForLayout1();
        }
        getInfo();
    }

    private void getInfo() {

    }

    private void addViews() {
        page_title = findViewById(R.id.tv_pageTitleEwallet);
        ewallet_name = findViewById(R.id.tv_ewall);
        phoneNumber = findViewById(R.id.tv_phoneNumber_add);
        spinnerEWalletName = findViewById(R.id.spinnerEWalletName);
        edt_username = findViewById(R.id.edtCusName);
        edt_phoneNumber = findViewById(R.id.edtPhoneNumber_add)
;
    }

    private void resetText() {
        page_title.setText("Add new Banking");
        ewallet_name.setText("Banking name");
        phoneNumber.setText("Account Number");

    }

    private void populateSpinnerForLayout1() {
        String[] walletNames = {"Momo", "Zalo Pay"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEWalletName.setAdapter(adapter);
    }

    private void populateSpinnerForLayout2() {
        String[] walletNames = {"Techcom Bank", "Vietcom Bank"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEWalletName.setAdapter(adapter);
    }
}
