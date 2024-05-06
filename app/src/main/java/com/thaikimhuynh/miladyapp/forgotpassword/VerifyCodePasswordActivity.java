package com.thaikimhuynh.miladyapp.forgotpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.signup.SignUpActivity;

public class VerifyCodePasswordActivity extends AppCompatActivity {

    private EditText edtVerificationCode;
    private Button btnVerify;
    private String verificationId;
    private TextView txtMessageError;
    private FirebaseAuth mAuth;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code_password);
        addViews();
    }

    private void addViews() {
        edtVerificationCode = findViewById(R.id.edtVerify);
        btnVerify = findViewById(R.id.btnVerify);
        txtMessageError = findViewById(R.id.txtMessageError);

        // Nhận verificationId từ Intent
        verificationId = getIntent().getStringExtra("verificationId");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        // Khởi tạo FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Xử lý sự kiện khi nhấn nút Verify
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy mã xác minh từ người dùng
                String code = edtVerificationCode.getText().toString().trim();

                // Kiểm tra xem mã xác minh có rỗng hay không
                if (!code.isEmpty()) {
                    // Nếu không rỗng, gửi mã xác minh đến Firebase để xác minh
                    verifyPhoneNumberWithCode(verificationId, code);
                } else {
                    // Nếu rỗng, hiển thị thông báo cho người dùng
                    txtMessageError.setText("Please enter verification code");
                }
            }
        });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // Tạo PhoneAuthCredential object từ mã xác minh và verificationId
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // Đăng nhập bằng PhoneAuthCredential
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            txtMessageError.setVisibility(View.GONE);
                            // Nếu xác minh thành công, chuyển hướng người dùng đến NewPasswordActivity
                            // Truyền số điện thoại qua Intent
                            Intent intent = new Intent(VerifyCodePasswordActivity.this, NewPasswordActivity.class);
                            intent.putExtra("phoneNumber", phoneNumber);
                            startActivity(intent);
                            finish(); // Kết thúc hoạt động hiện tại
                        } else {
                            // Xử lý khi xác minh không thành công
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                txtMessageError.setText("You have entered failed code. Please fill true code");
                            } else {
                                Toast.makeText(VerifyCodePasswordActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void gotoSignUp(View view) {
        // Mở trang SignUpActivity
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
