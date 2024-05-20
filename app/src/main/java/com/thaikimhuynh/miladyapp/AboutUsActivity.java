package com.thaikimhuynh.miladyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutUsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final String TAG = "FIREBASE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Read data from Firebase and display on the map
        readDataFromFirebase();
    }

    private void readDataFromFirebase() {
        DatabaseReference locationsRef = FirebaseDatabase.getInstance().getReference("Location");

        locationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Truy cập dữ liệu trong Firebase Realtime Database
                    String des = snapshot.child("des").getValue(String.class);
                    double latitude = snapshot.child("latitude").getValue(Double.class);
                    double longitude = snapshot.child("longitude").getValue(Double.class);

                    // Tạo LatLng từ dữ liệu lấy được
                    LatLng latLng = new LatLng(latitude, longitude);

                    // Tạo Marker và hiển thị trên bản đồ
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Trường Đại học Kinh tế Luật").snippet(des));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error reading data from Firebase", databaseError.toException());
            }
        });
        ImageView backImageView = findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo Intent để chuyển sang màn hình Help Center
                Intent helpCenterIntent = new Intent(AboutUsActivity.this, HelpCenterActivity.class);
                startActivity(helpCenterIntent);
                finish(); // Kết thúc màn hình About Us hiện tại
            }
        });

    }
}