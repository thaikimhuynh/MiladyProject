package com.thaikimhuynh.miladyapp.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.CartPaymentMethodActivity;
import com.thaikimhuynh.miladyapp.MyWalletActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.PaymentItem;

public class AddNewWalletCartActivity extends AppCompatActivity {
    TextView page_title, ewallet_name, phoneNumber;
    EditText edt_username, edt_phoneNumber;
    Spinner spinnerEWalletName;
    String imgLogo;
    String layout;
    String paymentMethodId;
    String account_number, name;
    Button btnAdd;


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
        addEvents();


    }

    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
                addNewPaymentAccount();
                Intent intent = new Intent(AddNewWalletCartActivity.this, SuccessfullyChangeCartActivity.class);
                startActivity(intent);

            }
        });
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void addNewPaymentAccount() {
        String userId = getUserId();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PaymentAccount");
        Log.d("mDatabase", mDatabase.toString());
        mDatabase.orderByChild("paymentId").addListenerForSingleValueEvent(new ValueEventListener() {
            String latestPaymentId = "0";

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("mDatabase", snapshot.toString() );

                for (DataSnapshot dtsnapshot : snapshot.getChildren())
                {
                    Log.d("dtsnapshot", dtsnapshot.toString());
                    String paymentId = dtsnapshot.child("paymentId").getValue(String.class);
                    Log.d("dtsnapshot", dtsnapshot.toString());

                    latestPaymentId = paymentId;
                    Log.d("latestPaymentId", latestPaymentId.toString());

                }

                int incrementalPaymentId = Integer.parseInt(latestPaymentId) + 1;
                String newPaymentId = String.valueOf(incrementalPaymentId);

                mDatabase.child(newPaymentId).child("accountNumber").setValue(account_number);
                mDatabase.child(newPaymentId).child("name").setValue(name);
                mDatabase.child(newPaymentId).child("paymentId").setValue(newPaymentId);
                mDatabase.child(newPaymentId).child("paymentMethodId").setValue(paymentMethodId);
                mDatabase.child(newPaymentId).child("userId").setValue(userId);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getInfo() {
        name = edt_username.getText().toString();
        account_number = edt_phoneNumber.getText().toString();
        String ewallet_name = spinnerEWalletName.getSelectedItem().toString();
        switch (ewallet_name) {
            case "Momo":
                imgLogo = "https://firebasestorage.googleapis.com/v0/b/miladyproject.appspot.com/o/ic_momo.png?alt=media&token=6aa854ef-6435-4416-9d70-52e67b391b37";
                break;
            case "Zalo Pay":
                imgLogo = "https://firebasestorage.googleapis.com/v0/b/miladyproject.appspot.com/o/ic_zlp.png?alt=media&token=0d230730-7f2c-41bb-8417-d8548c9e1e59";
                break;
            case "Techcom Bank":
                imgLogo = "https://firebasestorage.googleapis.com/v0/b/miladyproject.appspot.com/o/ic_tcb.png?alt=media&token=56b36c64-1adb-4df1-b33a-9aa2fe243212";
                break;
            default:
                imgLogo = "https://firebasestorage.googleapis.com/v0/b/miladyproject.appspot.com/o/ic_tcb.png?alt=media&token=56b36c64-1adb-4df1-b33a-9aa2fe243212";
                break;
        }
        switch (ewallet_name) {
            case "Momo":
                paymentMethodId = "2";
                break;
            case "Zalo Pay":
                paymentMethodId = "3";
                break;
            case "Techcom Bank":
                paymentMethodId = "4";
                break;
            default:
                paymentMethodId = "5";
                break;
        }
        if(ewallet_name.equals("Momo") || ewallet_name.equals("Zalo Pay")) {
            layout = "1";
        } else {
            layout = "2";
        }

    }

    private void addViews() {
        page_title = findViewById(R.id.tv_pageTitleEwallet);
        ewallet_name = findViewById(R.id.tv_ewall);
        phoneNumber = findViewById(R.id.tv_phoneNumber_add);
        spinnerEWalletName = findViewById(R.id.spinnerEWalletName);
        edt_username = findViewById(R.id.edtCusName);
        edt_phoneNumber = findViewById(R.id.edtPhoneNumber_add);
        btnAdd = findViewById(R.id.btnAddNewEwall);

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

    public void backtoMyWallet(View view) {
        Intent intent = new Intent(AddNewWalletCartActivity.this, CartPaymentMethodActivity.class);
        startActivity(intent);
    }
}