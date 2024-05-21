package com.thaikimhuynh.miladyapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.thaikimhuynh.miladyapp.MainActivity;
import com.thaikimhuynh.miladyapp.ProductListActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.databinding.ActivityNewPasswordBinding;
import com.thaikimhuynh.miladyapp.forgotpassword.ForgotPasswordActivity;
import com.thaikimhuynh.miladyapp.login.LoginActivity;
import com.thaikimhuynh.miladyapp.signup.SignUpActivity;

import java.util.Objects;

public class AdminLoginActivity extends AppCompatActivity {
    EditText edtPhoneNumber, edtPassword;
    Button btnLogin;
    TextView txtForgotPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        edtPhoneNumber= findViewById(R.id.edtPhoneNumber);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        txtForgotPassWord=findViewById(R.id.txtForgotPassWord);
        txtForgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminLoginActivity.this, NavigationAdminActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePhoneNumber() | !validatePassword()){

                }else {
                    checkPhoneNumber();
                }
            }
        });
    }

    private void checkPhoneNumber() {
        String AdminPhoneNumber = edtPhoneNumber.getText().toString().trim();
        String AdminPassWord = edtPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admin");
        Query checkUserDatabase = reference.orderByChild("phoneNumber").equalTo(AdminPhoneNumber);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    edtPhoneNumber.setError(null);
                    String PasswordFromDB= snapshot.child(AdminPhoneNumber).child("password").getValue(String.class);
                    if (!Objects.equals(PasswordFromDB,AdminPassWord)){
                        edtPhoneNumber.setError(null);
                        Intent intent= new Intent(AdminLoginActivity.this, NavigationAdminActivity.class);
                        startActivity(intent);
                    } else {
                        edtPassword.setError("Wrong password or phonenumber!");
                        edtPassword.requestFocus();
                    }
                }else {
                    edtPhoneNumber.setError("Unregistered Phone Number");
                    edtPhoneNumber.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validatePhoneNumber() {
        String val= edtPhoneNumber.getText().toString();
        if(val.isEmpty()){
            edtPhoneNumber.setError("Please fill in your Phone Number");
            return  false;
        } else {
            edtPhoneNumber.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val= edtPassword.getText().toString();
        if(val.isEmpty()){
            edtPassword.setError("Please fill in Password");
            return  false;
        } else {
            edtPassword.setError(null);
            return true;
        }
    }
}