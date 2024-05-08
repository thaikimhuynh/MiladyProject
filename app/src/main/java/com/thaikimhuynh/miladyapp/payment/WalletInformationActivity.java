package com.thaikimhuynh.miladyapp.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.PaymentItem;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.List;

public class WalletInformationActivity extends AppCompatActivity {
    TextView ewallet_name, user_name, phoneNumber, pageTitle, tvName, tvphoneNumber;
    ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_information);
        addViews();
       String layout = getIntent().getStringExtra("layout");
       if (layout.equals("2")){
            resetText(layout);

        }
        loadPaymentInfo();

    }

    private void resetText(String layout) {
        pageTitle.setText("Banking Information");
        tvName.setText("Bank name");
        tvphoneNumber.setText("Account Number");

    }

    private void loadPaymentInfo() {
        PaymentItem paymentItem = (PaymentItem) getIntent().getSerializableExtra("paymentItem");


        ewallet_name.setText(paymentItem.getWallet_name());
        user_name.setText(paymentItem.getUser_name());
        phoneNumber.setText(paymentItem.getPhoneNumber());
        String imageUrl = paymentItem.getImg_logo();
        Glide.with(this)
                .load(imageUrl)
                .into(logoImageView);






    }

    private void addViews() {
        ewallet_name = findViewById(R.id.edtWalletInfo);
        tvName = findViewById(R.id.wallet_name);
        user_name = findViewById(R.id.edt_UserName);
        phoneNumber = findViewById(R.id.edt_phoneNum);
        pageTitle = findViewById(R.id.tv_PageTitle);
        tvphoneNumber = findViewById(R.id.txt_phoneNum);
        logoImageView = findViewById(R.id.img_logo);

    }
}