package com.thaikimhuynh.miladyapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.MainActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.admin.AdminProductManagementActivity;
import com.thaikimhuynh.miladyapp.forgotpassword.ForgotPasswordActivity;
import com.thaikimhuynh.miladyapp.forgotpassword.ForgotPasswordActivity;
import com.thaikimhuynh.miladyapp.signup.SignUpActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText edtPhoneNumber, edtPassword;
    Button btnLogin;
    TextView txtForgotPassWord, txtSignUp;
    String userId;
    ImageView imgShowPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtPhoneNumber= findViewById(R.id.edtPhoneNumber);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        txtForgotPassWord=findViewById(R.id.txtForgotPassWord);
        txtSignUp = findViewById(R.id.txtSignUp);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        txtForgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    // Nếu đang ẩn mật khẩu, chuyển sang hiển thị
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    // Thay đổi hình ảnh của ImageView thành ảnh ẩn mật khẩu
                    imgShowPassword.setImageResource(R.mipmap.ic_eye_on);
                } else {
                    // Nếu đang hiển thị mật khẩu, chuyển sang ẩn
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // Thay đổi hình ảnh của ImageView thành ảnh hiển thị mật khẩu
                    imgShowPassword.setImageResource(R.mipmap.ic_eye_off);
                }

                // Đặt vị trí con trỏ về cuối chuỗi
                edtPassword.setSelection(edtPassword.getText().length());
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

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    public Boolean validatePhoneNumber(){
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
    public void checkPhoneNumber(){
        String UserPhoneNumber = edtPhoneNumber.getText().toString().trim();
        String UserPassWord = edtPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUserDatabase = reference.orderByChild("phoneNumber").equalTo(UserPhoneNumber);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Admin");
        Query checkAdminDatabase = reference2.orderByChild("phoneNumber").equalTo(UserPhoneNumber);

        checkAdminDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    edtPhoneNumber.setError(null);
                    String PasswordFromDB= snapshot.child(UserPhoneNumber).child("password").getValue(String.class);
                    if (PasswordFromDB.equals(UserPassWord)){
                        edtPhoneNumber.setError(null);
                        Intent intent= new Intent(LoginActivity.this, AdminProductManagementActivity.class);
                        startActivity(intent);
                    } else {
                        edtPassword.setError("Wrong password or phone number!");
                        edtPassword.requestFocus();
                    }
                }else {
                   checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(snapshot.exists()){
                               Log.d("phonenumber", edtPhoneNumber.toString());
                               Log.d("Userpassword", edtPassword.toString());

                               edtPhoneNumber.setError(null);
                               String PasswordFromDB= snapshot.child(UserPhoneNumber).child("password").getValue(String.class);
                               Log.d("DBpassword", PasswordFromDB);

                               if (Objects.equals(PasswordFromDB,UserPassWord)){
                                   edtPhoneNumber.setError(null);
                                   userId = snapshot.child(UserPhoneNumber).child("id").getValue(String.class);
                                   Toast.makeText(LoginActivity.this, "User ID: " + userId, Toast.LENGTH_SHORT).show();

                                   sharedPreferences();
                                   Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                                   startActivity(intent);
                               }else {
                                   edtPassword.setError("Wrong password or phone number!");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void sharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", userId);
        editor.apply();

    }

}