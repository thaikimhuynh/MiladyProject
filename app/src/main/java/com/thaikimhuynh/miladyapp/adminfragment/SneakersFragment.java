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
import com.thaikimhuynh.miladyapp.adapter.AdminSneakersAdapter;
import com.thaikimhuynh.miladyapp.admin.AdminUpdateProductActivity;
import com.thaikimhuynh.miladyapp.model.AdminSneakers;

import java.util.ArrayList;
import java.util.List;

public class SneakersFragment extends Fragment {
    private ListView listView;
    private AdminSneakersAdapter adapter;
    private List<AdminSneakers> sneakersList;
    private DatabaseReference databaseReference;
    private static final int UPDATE_PRODUCT_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sneakers, container, false);
        listView = view.findViewById(R.id.lv_sneakers);
        sneakersList = new ArrayList<>();
        adapter = new AdminSneakersAdapter(getContext(), sneakersList, new AdminSneakersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdminSneakers sneakers) {
                Intent intent = new Intent(getContext(), AdminUpdateProductActivity.class);
                intent.putExtra("itemId", sneakers.getItemId());
                intent.putExtra("id", sneakers.getId());
                intent.putExtra("name", sneakers.getName());
                intent.putExtra("price", sneakers.getPrice());
                intent.putExtra("stock", sneakers.getStock());
                intent.putExtra("picUrl", sneakers.getPicUrl());
                intent.putExtra("description", sneakers.getDescription());
                startActivityForResult(intent, UPDATE_PRODUCT_REQUEST);
            }
        });

        // Đặt sự kiện cho onDeleteClickListener
        adapter.setOnDeleteClickListener(new AdminSneakersAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(AdminSneakers sneakers) {
                // Xử lý xóa sản phẩm
                deleteProduct(sneakers);
            }
        });

        listView.setAdapter(adapter);
        return view;
    }

    private void deleteProduct(AdminSneakers sneakers) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Items").child(sneakers.getItemId());
        ref.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                // Xóa sản phẩm khỏi danh sách và cập nhật giao diện người dùng
                sneakersList.remove(sneakers);
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
        databaseReference.orderByChild("category_id").equalTo("C3").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sneakersList.clear();
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

                    AdminSneakers sneakers = new AdminSneakers();
                    sneakers.setItemId(itemId);
                    sneakers.setId(id);
                    sneakers.setName(name);
                    sneakers.setPrice(price);
                    sneakers.setStock(stock);
                    sneakers.setDescription(description);
                    sneakers.setPicUrl(picUrl);
                    sneakersList.add(sneakers);
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
            for (AdminSneakers sneakers : sneakersList) {
                if (sneakers.getId().equals(id)) {
                    sneakers.setName(name);
                    sneakers.setPrice(price);
                    sneakers.setStock(stock);
                    sneakers.setPicUrl(picUrl);
                    sneakers.setDescription(description);
                    break;
                }
            }

            // Cập nhật giao diện người dùng
            adapter.notifyDataSetChanged();
        }
    }
}
