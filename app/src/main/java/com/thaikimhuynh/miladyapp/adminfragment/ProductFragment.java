package com.thaikimhuynh.miladyapp.adminfragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
// Trong ProductFragment.java

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.admin.AdminAddProductActivity;
import com.thaikimhuynh.miladyapp.admin.AdminListProductActivity;

public class ProductFragment extends Fragment {
    ImageView icListProduct;
    ImageView icaddproduct;
    TextView edtheels, edtsandals, edtsneakers, edtboots, totalProduct;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items");

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        icListProduct = view.findViewById(R.id.ic_list);
        icListProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển qua trang AdminListProductActivity khi bấm vào ảnh ic_list_product
                Intent intent = new Intent(getActivity(), AdminListProductActivity.class);
                startActivity(intent);
            }
        });

        icaddproduct = view.findViewById(R.id.ic_add_new);
        icaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển qua trang AdminAddProductActivity khi bấm vào ảnh ic_add_new
                Intent intent = new Intent(getActivity(), AdminAddProductActivity.class);
                startActivity(intent);
            }
        });

        // Lấy tham chiếu đến các EditText fields
        edtheels = view.findViewById(R.id.edt_heels);
        edtsandals = view.findViewById(R.id.edt_sandals);
        edtsneakers = view.findViewById(R.id.edt_sneakers);
        edtboots = view.findViewById(R.id.edt_boots);
        totalProduct = view.findViewById(R.id.total_product);

        // Đếm và hiển thị số lượng sản phẩm cho từng danh mục
        countProductByCategory();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Lắng nghe sự thay đổi trên node "products"
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Gọi phương thức để đếm và cập nhật số lượng sản phẩm
                countAndSetProductCounts(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }

    private void countProductByCategory() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Items");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countAndSetProductCounts(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void countAndSetProductCounts(DataSnapshot dataSnapshot) {
        int countC1 = 0, countC2 = 0, countC3 = 0, countC4 = 0;

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            String categoryId = snapshot.child("category_id").getValue(String.class);
            if (categoryId != null) {
                switch (categoryId) {
                    case "C1":
                        countC1++;
                        break;
                    case "C2":
                        countC2++;
                        break;
                    case "C3":
                        countC3++;
                        break;
                    case "C4":
                        countC4++;
                        break;
                    default:
                        // Handle other category IDs if needed
                        break;
                }
            }
        }

        // Set counts to EditText fields
        edtheels.setText(String.valueOf(countC1));
        edtsandals.setText(String.valueOf(countC2));
        edtsneakers.setText(String.valueOf(countC3));
        edtboots.setText(String.valueOf(countC4));

        // Tính tổng và đặt vào TextView totalProduct
        int total = countC1 + countC2 + countC3 + countC4;
        totalProduct.setText(String.valueOf(total));
    }
}
