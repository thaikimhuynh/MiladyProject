package com.thaikimhuynh.miladyapp.signup;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.login.LoginActivity;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = VerifyAccountActivity.class.getName();
    EditText edtPhoneNumber, edtEmail, edtPassword,edtFullName;
    TextView txtLogin;
    Button btnSignUp;
    String phoneNumber;
    String Email;
    String Password;
    String FullName;
    private DatabaseReference mDatabase;
    ImageView imgShowPassword;

    private static final String COUNTRY_CODE = "+84";


    FirebaseDatabase database;
    DatabaseReference reference;
    private TextView txtMessageError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addView();
        database= FirebaseDatabase.getInstance();
        reference= database.getReference("User");
    }

    private void addView() {
        edtPhoneNumber= findViewById(R.id.edtUserName);
        btnSignUp=findViewById(R.id.btnSignUp);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullName = findViewById(R.id.edtFullName);
        txtMessageError = findViewById(R.id.txtMessageError);
        txtLogin=findViewById(R.id.txtLogin);
        txtLogin.setPaintFlags(txtLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        imgShowPassword = findViewById(R.id.imgShowPassword);
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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = edtPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    txtMessageError.setText("Please enter a phone number");
                } else if (!isValidPhoneNumber(phoneNumber)) {
                    txtMessageError.setText("Please enter a valid phone number");
                } else
                {
                    if (phoneNumber.startsWith("+84"))
                    {
                        phoneNumber = "0" + phoneNumber.substring(3);
                    }
                }
                Email = edtEmail.getText().toString();
                FullName=edtFullName.getText().toString();
                Password = edtPassword.getText().toString();
                checkPhoneNumberExist(phoneNumber);

            }
        });
    }

    private void checkPhoneNumberExist(String phoneNumber) {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean isPhoneNumberExist = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String storedPhone = String.valueOf(userSnapshot.child("phoneNumber").getValue());

                    // Kiểm tra xem số điện thoại từ DataSnapshot có trùng khớp với số điện thoại người dùng nhập không
                    if (TextUtils.equals(storedPhone, phoneNumber)) {
                        isPhoneNumberExist = true;
                        txtMessageError.setText("This phone number was registered before");
                        break;
                    }
                }

                if (!isPhoneNumberExist)
                {
                    String phoneWithCountryCode = COUNTRY_CODE + phoneNumber.substring(1);
                    Log.d("PhoneWithCountryCode", phoneWithCountryCode);

                    sendOTP(phoneWithCountryCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase Database", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void sendOTP(String phoneWithCountryCode) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneWithCountryCode,
                60, // Timeout duration
                TimeUnit.SECONDS,
                SignUpActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.d(TAG, "Sending intent to NewPasswordActivity with phoneNumber: " + phoneNumber);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(SignUpActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        Toast.makeText(SignUpActivity.this, "An OTP has been sent to your phone."
                                ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, VerifyAccountActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber); // Truyền data qua Intent
                        intent.putExtra("email",Email);
                        intent.putExtra("password",Password);
                        intent.putExtra("name",FullName);

                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                });
    }

    private boolean isValidPhoneNumber(final String phoneNumber) {
        return phoneNumber.matches("^(\\+84|0)\\d{9}$");
    }


    public void openLoginActivity(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}