package com.thaikimhuynh.miladyapp.adminfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.admin.AdminAddProductActivity;
import com.thaikimhuynh.miladyapp.admin.AdminListProductActivity;
import com.thaikimhuynh.miladyapp.login.WelcomeActivity;

public class ProductFragment extends Fragment {
    ImageView icListProduct;
    ImageView icAddProduct;
    TextView edtHeels, edtSandals, edtSneakers, edtBoots, totalProduct;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items");
    View view5;
    ImageView imageView8;

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        imageView8 = view.findViewById(R.id.imageView8);

        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        // Initialize ImageView and set OnClickListener for product list
        icListProduct = view.findViewById(R.id.ic_list);
        icListProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AdminListProductActivity when the image is clicked
                Intent intent = new Intent(getActivity(), AdminListProductActivity.class);
                startActivity(intent);
            }
        });

        // Initialize ImageView and set OnClickListener for adding product
        icAddProduct = view.findViewById(R.id.ic_add_new);
        icAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AdminAddProductActivity when the image is clicked
                Intent intent = new Intent(getActivity(), AdminAddProductActivity.class);
                startActivity(intent);
            }
        });

        // Initialize view5 and set OnClickListener for product list
        view5 = view.findViewById(R.id.view5);
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AdminListProductActivity when the view is clicked
                Intent intent = new Intent(getActivity(), AdminListProductActivity.class);
                startActivity(intent);
            }
        });

        // Initialize TextView fields for categories and total product count
        edtHeels = view.findViewById(R.id.edt_heels);
        edtSandals = view.findViewById(R.id.edt_sandals);
        edtSneakers = view.findViewById(R.id.edt_sneakers);
        edtBoots = view.findViewById(R.id.edt_boots);
        totalProduct = view.findViewById(R.id.total_product);

        // Count and display the number of products per category
        countProductByCategory();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Listen for changes on the "Items" node
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Call method to count and update the number of products
                countAndSetProductCounts(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
            }
        });
    }

    private void countProductByCategory() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countAndSetProductCounts(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
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

        // Set counts to TextView fields
        edtHeels.setText(String.valueOf(countC1));
        edtSandals.setText(String.valueOf(countC2));
        edtSneakers.setText(String.valueOf(countC3));
        edtBoots.setText(String.valueOf(countC4));

        // Calculate total and set to totalProduct TextView
        int total = countC1 + countC2 + countC3 + countC4;
        totalProduct.setText(String.valueOf(total));
    }
}
