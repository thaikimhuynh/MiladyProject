package com.thaikimhuynh.miladyapp.adminfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.AdminSandalsAdapter;
import com.thaikimhuynh.miladyapp.admin.AdminUpdateProductActivity;
import com.thaikimhuynh.miladyapp.model.AdminSandals;

import java.util.ArrayList;
import java.util.List;

public class SandalsFragment extends Fragment {
    private ListView listView;
    private AdminSandalsAdapter adapter;
    private List<AdminSandals> sandalsList;
    private DatabaseReference databaseReference;
    private static final int UPDATE_PRODUCT_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sandals, container, false);
        listView = view.findViewById(R.id.lv_sandals);
        sandalsList = new ArrayList<>();
        adapter = new AdminSandalsAdapter(getContext(), sandalsList, new AdminSandalsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminSandals sandals) {
                Intent intent = new Intent(getContext(), AdminUpdateProductActivity.class);
                intent.putExtra("itemId", sandals.getItemId());
                intent.putExtra("id", sandals.getId());
                intent.putExtra("name", sandals.getName());
                intent.putExtra("price", sandals.getPrice());
                intent.putExtra("stock", sandals.getStock());
                intent.putExtra("picUrl", sandals.getPicUrl());
                intent.putExtra("description", sandals.getDescription());
                startActivityForResult(intent, UPDATE_PRODUCT_REQUEST);
            }
        });

        // Đặt sự kiện cho onDeleteClickListener
        adapter.setOnDeleteClickListener(new AdminSandalsAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(AdminSandals sandals) {
                // Xử lý xóa sản phẩm
                deleteProduct(sandals);
            }
        });

        listView.setAdapter(adapter);
        return view;
    }

    private void deleteProduct(AdminSandals sandals) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Items").child(sandals.getItemId());
        ref.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                // Xóa sản phẩm khỏi danh sách và cập nhật giao diện người dùng
                sandalsList.remove(sandals);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Items");
        retrieveData();
    }

    private void retrieveData() {
        databaseReference.orderByChild("category_id").equalTo("C2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sandalsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemId = snapshot.getKey();
                    String id = snapshot.child("id").getValue(String.class);
                    String name = snapshot.child("title").getValue(String.class);
                    Double price = snapshot.child("price").getValue(Double.class);
                    Integer stock = snapshot.child("stock").getValue(Integer.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String picUrl = "";
                    if (snapshot.child("picUrl").exists()) {
                        picUrl = snapshot.child("picUrl").child("0").getValue(String.class);
                    }

                    AdminSandals sandals = new AdminSandals();
                    sandals.setItemId(itemId);
                    sandals.setId(id);
                    sandals.setName(name);
                    sandals.setPrice(price);
                    sandals.setStock(stock);
                    sandals.setDescription(description);
                    sandals.setPicUrl(picUrl);
                    sandalsList.add(sandals);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_PRODUCT_REQUEST && resultCode == Activity.RESULT_OK) {
            // Lấy thông tin sản phẩm được cập nhật từ kết quả trả về
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");
            Double price = data.getDoubleExtra("price", 0.0);
            Integer stock = data.getIntExtra("stock", 0);
            String picUrl = data.getStringExtra("picUrl");
            String description = data.getStringExtra("description");

            // Cập nhật sản phẩm trong danh sách với thông tin mới
            for (AdminSandals sandals : sandalsList) {
                if (sandals.getId().equals(id)) {
                    sandals.setName(name);
                    sandals.setPrice(price);
                    sandals.setStock(stock);
                    sandals.setPicUrl(picUrl);
                    sandals.setDescription(description);
                    break;
                }
            }

            // Cập nhật giao diện người dùng
            adapter.notifyDataSetChanged();
        }
    }
}
