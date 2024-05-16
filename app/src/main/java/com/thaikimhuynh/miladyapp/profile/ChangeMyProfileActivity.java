package com.thaikimhuynh.miladyapp.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;

public class ChangeMyProfileActivity extends AppCompatActivity {
    EditText edtProfileName, edtProfileMail,edtProfilePhoneNumber;
    Button btnLogin;
    DatabaseReference database;
    String name, password,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_profile);
        edtProfileName=findViewById(R.id.edtProfileName);
        edtProfileMail= findViewById(R.id.edtProfileEmail);
        edtProfilePhoneNumber=findViewById(R.id.edtProfilePhoneNumber);
        btnLogin =findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtProfileName.getText().toString().trim();
                email = edtProfileMail.getText().toString().trim();
                password = edtProfilePhoneNumber.getText().toString().trim();
                if (!name.isEmpty()) {
                    updateName();}
                if(!email.isEmpty()) {
                    updateEmail();
                }
                if (!password.isEmpty()){
                    updatePassword();
                }
            }
        });
    }
    private void updateName() {
        String userId = getUserId();
        database = FirebaseDatabase.getInstance().getReference();
        database.child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("name").setValue(name);
                Toast.makeText(ChangeMyProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ChangeMyProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }
    private void updateEmail() {
        String userId = getUserId();
        database = FirebaseDatabase.getInstance().getReference();
        database.child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("email").setValue(email);
                Toast.makeText(ChangeMyProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ChangeMyProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }
    private void updatePassword() {
        String userId = getUserId();
        database = FirebaseDatabase.getInstance().getReference();
        database.child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("password").setValue(password);
                Toast.makeText(ChangeMyProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ChangeMyProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }

    private String getUserId() {
            SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
            return sharedPreferences.getString("user_id", "");
        }
    }

