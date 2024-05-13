package com.thaikimhuynh.miladyapp.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thaikimhuynh.miladyapp.R;

public class ChangeMyProfileActivity extends AppCompatActivity {
    EditText edtProfileName,edtProfileEmail,edtProfilePhoneNumber;
    Button btnLogin;
    String nameUser, emailUser, phoneNumber, phonenumberUser;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_profile);

        reference= FirebaseDatabase.getInstance().getReference("User");
        edtProfileName=findViewById(R.id.edtProfileName);
        edtProfileEmail= findViewById(R.id.edtProfileEmail);
        edtProfilePhoneNumber=findViewById(R.id.edtProfilePhoneNumber);
        btnLogin =findViewById(R.id.btnLogin);
        showData();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailChange() || isNameChange() || isPhoneNumberChange()){
                    Toast.makeText(ChangeMyProfileActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChangeMyProfileActivity.this,"Save failed",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public boolean isEmailChange(){
        if(!emailUser.equals(edtProfileEmail.getText().toString())){
            reference.child(phonenumberUser).child("email").setValue(edtProfileEmail.getText().toString());
            emailUser = edtProfileEmail.getText().toString();
            return true;
        }else {
            return false;
        }
    }
    public boolean isNameChange(){
        if(!nameUser.equals(edtProfileName.getText().toString())){
            reference.child(phonenumberUser).child("name").setValue(edtProfileName.getText().toString());
            nameUser = edtProfileName.getText().toString();
            return true;
        }else {
            return false;
        }
    }
    public boolean isPhoneNumberChange(){
        if(!phoneNumber.equals(edtProfilePhoneNumber.getText().toString())){
            reference.child(phonenumberUser).child("email").setValue(edtProfilePhoneNumber.getText().toString());
            phoneNumber = edtProfilePhoneNumber.getText().toString();
            return true;
        }else {
            return false;
        }
    }
    public void showData(){
        Intent intent=getIntent();
        nameUser= intent.getStringExtra("name");
        emailUser= intent.getStringExtra("email");
        phoneNumber=intent.getStringExtra("phoneNumber");
        phonenumberUser=intent.getStringExtra("phoneNumber");

        edtProfileName.setText(nameUser);
        edtProfileEmail.setText(emailUser);
        edtProfilePhoneNumber.setText(phoneNumber);
    }

}