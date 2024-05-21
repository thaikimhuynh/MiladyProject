package com.thaikimhuynh.miladyapp.adminfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.admin.AdminListOrderActivity;
import com.thaikimhuynh.miladyapp.login.WelcomeActivity;
//import com.thaikimhuynh.miladyapp.admin.AdminListOrderActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    ImageView icListOrder;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView total_order,heel_product,sandal_product,ondelivery_order,sneaker_product,cancled_order;
    ImageView imageView13;
    ImageView imageView8;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        total_order= view.findViewById(R.id.total_order);
        heel_product= view.findViewById(R.id.heel_product);
        sandal_product= view.findViewById(R.id.sandal_product);
        ondelivery_order= view.findViewById(R.id.ondelivery_order);
        sneaker_product= view.findViewById(R.id.sneaker_product);
        cancled_order= view.findViewById(R.id.cancled_order);
        imageView13= view.findViewById(R.id.imageView13);
        imageView8 = view.findViewById(R.id.imageView8);
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AdminListOrderActivity.class);
                startActivity(intent);
            }
        });
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalOrders = 0;
                int toConfirmCount = 0;
                int preparingCount = 0;
                int onDeliveryCount = 0;
                int completedCount = 0;
                int cancelledCount = 0;
                for (DataSnapshot orderSnapshot : snapshot.getChildren()){
                    String orderStatus = orderSnapshot.child("orderStatus").getValue(String.class);
                    totalOrders++;
                    switch (orderStatus) {
                        case "To Confirm":
                            toConfirmCount++;
                            break;
                        case "Preparing":
                            preparingCount++;
                            break;
                        case "On Delivery":
                            onDeliveryCount++;
                            break;
                        case "Completed":
                            completedCount++;
                            break;
                        case "Cancelled":
                            cancelledCount++;
                            break;
                    }
                }
                total_order.setText(String.valueOf(totalOrders));
                heel_product.setText(String.valueOf(toConfirmCount));
                sandal_product.setText(String.valueOf(preparingCount));
                ondelivery_order.setText(String.valueOf(onDeliveryCount));
                sneaker_product.setText(String.valueOf(completedCount));
                cancled_order.setText(String.valueOf(cancelledCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











        return view;
    }
}
