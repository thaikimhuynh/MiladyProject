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
import com.thaikimhuynh.miladyapp.adapter.AdminHeelsAdapter;
import com.thaikimhuynh.miladyapp.admin.AdminUpdateProductActivity;
import com.thaikimhuynh.miladyapp.model.AdminHeels;

import java.util.ArrayList;
import java.util.List;

public class HeelsFragment extends Fragment {
    private ListView listView;
    private AdminHeelsAdapter adapter;
    private List<AdminHeels> heelsList;
    private DatabaseReference databaseReference;
    private static final int UPDATE_PRODUCT_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heels, container, false);
        listView = view.findViewById(R.id.lv_heels);
        heelsList = new ArrayList<>();
        adapter = new AdminHeelsAdapter(getContext(), heelsList, new AdminHeelsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminHeels heels) {
                Intent intent = new Intent(getContext(), AdminUpdateProductActivity.class);
                intent.putExtra("itemId", heels.getItemId());
                intent.putExtra("id", heels.getId());
                intent.putExtra("name", heels.getName());
                intent.putExtra("price", heels.getPrice());
                intent.putExtra("stock", heels.getStock());
                intent.putExtra("picUrl", heels.getPicUrl());
                intent.putExtra("description", heels.getDescription());
                startActivityForResult(intent, UPDATE_PRODUCT_REQUEST);
            }
        });

        // Đặt sự kiện cho onDeleteClickListener
        adapter.setOnDeleteClickListener(new AdminHeelsAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(AdminHeels heels) {
                // Xử lý xóa sản phẩm
                deleteProduct(heels);
            }
        });

        listView.setAdapter(adapter);
        return view;
    }

    private void deleteProduct(AdminHeels heels) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Items").child(heels.getItemId());
        ref.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                // Xóa sản phẩm khỏi danh sách và cập nhật giao diện người dùng
                heelsList.remove(heels);
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
        databaseReference.orderByChild("category_id").equalTo("C1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                heelsList.clear();
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

                    AdminHeels heels = new AdminHeels();
                    heels.setItemId(itemId);
                    heels.setId(id);
                    heels.setName(name);
                    heels.setPrice(price);
                    heels.setStock(stock);
                    heels.setDescription(description);
                    heels.setPicUrl(picUrl);
                    heelsList.add(heels);
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
            for (AdminHeels heels : heelsList) {
                if (heels.getId().equals(id)) {
                    heels.setName(name);
                    heels.setPrice(price);
                    heels.setStock(stock);
                    heels.setPicUrl(picUrl);
                    heels.setDescription(description);
                    break;
                }
            }

            // Cập nhật giao diện người dùng
            adapter.notifyDataSetChanged();
        }
    }
}
