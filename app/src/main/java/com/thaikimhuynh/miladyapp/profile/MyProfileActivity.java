package com.thaikimhuynh.miladyapp.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;

public class MyProfileActivity extends AppCompatActivity {
    TextView txtWalletName, txtMail,txtPhoneNumber;
    Button btnHadAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        txtWalletName= findViewById(R.id.txtWalletName);
        txtMail=findViewById(R.id.txtMail);
        txtPhoneNumber=findViewById(R.id.txtPhoneNumber);
        btnHadAccount=findViewById(R.id.btnHadAccount);
        showuserdata();
        btnHadAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passUserData();
            }
        });
    }

    private void showuserdata() {
        Intent intent= getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone= intent.getStringExtra("phoneNumber");
        txtWalletName.setText(name);
        txtMail.setText(email);
        txtPhoneNumber.setText(phone);

    }
    public void passUserData(){
        String userPhoneNumber= txtPhoneNumber.getText().toString().trim();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("User");
        Query checkUserDatabase= reference.orderByChild("phonenumber").equalTo(userPhoneNumber);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String namefromDB = snapshot.child(userPhoneNumber).child("name").getValue(String.class);
                    String emailfromDB = snapshot.child(userPhoneNumber).child("email").getValue(String.class);
                    String phonenumberfromDB = snapshot.child(userPhoneNumber).child("phoneNumber").getValue(String.class);
                    Intent intent= new Intent(MyProfileActivity.this, ChangeMyProfileActivity.class);
                    startActivity(intent);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}