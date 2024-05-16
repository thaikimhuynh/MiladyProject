package com.thaikimhuynh.miladyapp.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;

public class MyProfileActivity extends AppCompatActivity {
    EditText txtWalletName, txtMail,txtPhoneNumber;
    Button btnHadAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        txtWalletName= findViewById(R.id.txtWalletName);
        txtMail=findViewById(R.id.txtMail);
        txtPhoneNumber=findViewById(R.id.txtPhoneNumber);
        btnHadAccount=findViewById(R.id.btnHadAccount);
        String userId = getUserId();
        loafprofile(userId);

//        showuserdata();
        btnHadAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent= new Intent(MyProfileActivity.this, ChangeMyProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loafprofile(String userId) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("User");
       reference.orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot snapshot1 : snapshot.getChildren()){
                   String name= snapshot1.child("name").getValue(String.class);
                   String phonenumber= snapshot1.child("phoneNumber").getValue(String.class);
                   String email = snapshot1.child("email").getValue(String.class);
                   txtWalletName.setText(name);
                   txtMail.setText(email);
                   txtPhoneNumber.setText(phonenumber);

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

//    private void showuserdata() {
//        String userPhoneNumber= txtPhoneNumber.getText().toString().trim();
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("User");
//        Query checkUserDatabase= reference.orderByChild("phonenumber").equalTo(userPhoneNumber);
////        Intent intent= getIntent();
////        String name = intent.getStringExtra("name");
////        String email = intent.getStringExtra("email");
////        String phone= intent.getStringExtra("phoneNumber");
//        txtWalletName.setText(name);
//        txtMail.setText(email);
//        txtPhoneNumber.setText(phone);

//    }

}