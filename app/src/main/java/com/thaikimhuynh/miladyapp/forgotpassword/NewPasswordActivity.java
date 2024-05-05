package com.thaikimhuynh.miladyapp.forgotpassword;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.login.LoginActivity;
import com.thaikimhuynh.miladyapp.signup.SignUpActivity;

public class NewPasswordActivity extends AppCompatActivity {
    String TAG = "FIREBASE";
    private EditText edtNewPassword, edtConfirmPassword;
    private Button btnConfirm;
    private TextView txtMessageError;
    private String phoneNumber;
    ImageView imgShowPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        // Lấy số điện thoại từ Intent
        if (getIntent().hasExtra("phoneNumber")) {
            phoneNumber = getIntent().getStringExtra("phoneNumber");
            Log.d(TAG, "Received phoneNumber in NewPasswordActivity: " + phoneNumber);
        } else {
            // Nếu không có số điện thoại, kết thúc activity và hiển thị thông báo lỗi
            Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        addViews();
    }

    private void addViews() {
        // Ánh xạ các view từ layout
        edtNewPassword = findViewById(R.id.edtNewPassword);
        txtMessageError = findViewById(R.id.txtMessageError);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnConfirm = findViewById(R.id.btnConfirm);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNewPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    // Nếu đang ẩn mật khẩu, chuyển sang hiển thị
                    edtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    // Thay đổi hình ảnh của ImageView thành ảnh ẩn mật khẩu
                    imgShowPassword.setImageResource(R.mipmap.ic_eye_on);
                } else {
                    // Nếu đang hiển thị mật khẩu, chuyển sang ẩn
                    edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // Thay đổi hình ảnh của ImageView thành ảnh hiển thị mật khẩu
                    imgShowPassword.setImageResource(R.mipmap.ic_eye_off);
                }

                // Đặt vị trí con trỏ về cuối chuỗi
                edtNewPassword.setSelection(edtNewPassword.getText().length());
                edtConfirmPassword.setSelection(edtConfirmPassword.getText().length());
            }
        });

        // Thiết lập sự kiện click cho nút Confirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edtNewPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    txtMessageError.setText("Please enter new password");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    txtMessageError.setText("Passwords do not match");
                    return;
                }

                // Gọi phương thức để cập nhật mật khẩu mới
                updatePassword(newPassword);
            }
        });
    }

    // Cập nhật mật khẩu mới trong cơ sở dữ liệu Firebase
    private void updatePassword(String newPassword) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isPhoneNumberFound = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String storedPhoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);
                    if (!TextUtils.isEmpty(storedPhoneNumber) && storedPhoneNumber.equals(phoneNumber)) {
                        isPhoneNumberFound = true;
                        String userKey = userSnapshot.getKey();
                        DatabaseReference userRef = usersRef.child(userKey);
                        userRef.child("password").setValue(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Password updated successfully in Firebase database");
                                            Toast.makeText(getApplicationContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                            goToLogin();

                                        } else {
                                            Log.e(TAG, "Failed to update password in Firebase database", task.getException());
                                            txtMessageError.setText("Failed to update password. Please try again later.");
                                        }
                                    }
                                });
                        break;
                    }
                }
                if (!isPhoneNumberFound) {
                    txtMessageError.setText("Phone number not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error updating password in Firebase database: " + databaseError.getMessage());
                txtMessageError.setText("Failed to update password. Please try again later.");
            }
        });
    }
    private void goToLogin() {
        Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Kết thúc hoạt động hiện tại
    }


    // Chuyển đến trang đăng ký
    public void gotoSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
