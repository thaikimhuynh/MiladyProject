package com.thaikimhuynh.miladyapp.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.MyWalletActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.PaymentItem;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.List;

public class WalletInformationActivity extends AppCompatActivity {
    TextView ewallet_name, user_name, phoneNumber, pageTitle, tvName, tvphoneNumber;
    ImageView logoImageView;
    Button deleteBtn;
    ImageView back;
    String paymentItemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_information);
        addViews();
        PaymentItem paymentItem = (PaymentItem) getIntent().getSerializableExtra("payment_info");


        String layout_set = paymentItem.getLayout();
       if (layout_set.equals("2")){
            resetText(layout_set);

        }
       loadPaymentInfo(paymentItem);
       addEvents();

    }

    private void addEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletInformationActivity.this, MyWalletActivity.class);
                startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("PaymentAccount");
                mDatabase.orderByChild("paymentId").equalTo(paymentItemId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dsnapshot : snapshot.getChildren()){
                            dsnapshot.getRef().removeValue();



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(WalletInformationActivity.this, DeleteFailActivity.class);
                startActivity(intent);

            }
        });

    }

    private void resetText(String layout) {
        pageTitle.setText("Banking Information");
        tvName.setText("Bank name");
        tvphoneNumber.setText("Account Number");

    }

    private void loadPaymentInfo(PaymentItem paymentItem) {

        if (paymentItem != null) {
            // Log the PaymentItem details
            Log.d("PaymentItem", "Wallet: " + paymentItem.getWallet_name());
            Log.d("PaymentItem", "User: " + paymentItem.getUser_name());

            // Access and display payment info
            paymentItemId = paymentItem.getPaymentId();
            ewallet_name.setText(paymentItem.getWallet_name());
            user_name.setText(paymentItem.getUser_name());
            phoneNumber.setText(paymentItem.getPhoneNumber());
            String imageUrl = paymentItem.getImg_logo();
            Glide.with(this)
                    .load(imageUrl)
                    .into(logoImageView);
        } else {
            // Handle the case where paymentItem is null
            Log.e("PaymentInfo", "PaymentItem is null");
        }
    }


    private void addViews() {
        ewallet_name = findViewById(R.id.edtWalletInfo);
        tvName = findViewById(R.id.wallet_name);
        user_name = findViewById(R.id.edt_UserName);
        phoneNumber = findViewById(R.id.edt_phoneNum);
        pageTitle = findViewById(R.id.tv_PageTitle);
        tvphoneNumber = findViewById(R.id.txt_phoneNum);
        logoImageView = findViewById(R.id.img_logo);
        deleteBtn = findViewById(R.id.btnDelete);
        back = findViewById(R.id.btn_Back);


    }
}