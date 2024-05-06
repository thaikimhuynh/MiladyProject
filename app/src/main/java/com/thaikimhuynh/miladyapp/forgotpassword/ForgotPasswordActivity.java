package com.thaikimhuynh.miladyapp.forgotpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.signup.SignUpActivity;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String TAG = "FIREBASE";
    String phoneNumber;
    private DatabaseReference mDatabase;
    private EditText edtPhoneForgot;
    private TextView txtMessageError;
    private Button btnContinue;
    private static final String COUNTRY_CODE = "+84";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        addViews();
    }

    private void addViews() {
        edtPhoneForgot = findViewById(R.id.edtPhoneForgot);
        txtMessageError = findViewById(R.id.txtMessageError);
        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = edtPhoneForgot.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    txtMessageError.setText("Please enter a phone number");
                } else if (!isValidPhoneNumber(phoneNumber)) {
                    txtMessageError.setText("Please enter a valid phone number");
                } else {
                    if (phoneNumber.startsWith("+84")) {
                        phoneNumber = "0" + phoneNumber.substring(3);
                    }

                    checkPhoneNumberExist(phoneNumber);
                    txtMessageError.setVisibility(View.GONE);

                }
            }
        });
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Kiểm tra tính hợp lệ của số điện thoại
        return phoneNumber.matches("^(\\+84|0)\\d{9}$");
    }

    private void checkPhoneNumberExist(final String phoneNumber) {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean isPhoneNumberExist = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String storedPhone = String.valueOf(userSnapshot.child("phoneNumber").getValue());

                    // Kiểm tra xem số điện thoại từ DataSnapshot có trùng khớp với số điện thoại người dùng nhập không
                    if (TextUtils.equals(storedPhone, phoneNumber)) {
                        isPhoneNumberExist = true;
                        String phoneWithCountryCode = COUNTRY_CODE + storedPhone.substring(1);
                        sendOTP(phoneWithCountryCode);
                        break; // Thoát khỏi vòng lặp ngay sau khi tìm thấy số điện thoại trùng khớp
                    }
                }

                if (!isPhoneNumberExist) {
                    txtMessageError.setText("This phone number was not registered before");
                }            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase Database", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void sendOTP(String phoneWithCountryCode) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneWithCountryCode,
                60, // Timeout duration
                TimeUnit.SECONDS,
                ForgotPasswordActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.d(TAG, "Sending intent to NewPasswordActivity with phoneNumber: " + phoneNumber);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ForgotPasswordActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        Toast.makeText(ForgotPasswordActivity.this, "An OTP has been sent to your phone."
                                ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyCodePasswordActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber); // Truyền số điện thoại qua Intent

                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                });
    }

    public void gotoSignUp(View view) {
        // Mở trang SignUpActivity
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
