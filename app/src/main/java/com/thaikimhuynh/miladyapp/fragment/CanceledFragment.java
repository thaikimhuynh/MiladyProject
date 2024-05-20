package com.thaikimhuynh.miladyapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.FeedbackActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.OrderAdapter;
import com.thaikimhuynh.miladyapp.model.Order;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CanceledFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CanceledFragment extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    ArrayList<Order> orders;

    public CanceledFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CanceledFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CanceledFragment newInstance(String param1, String param2) {
        CanceledFragment fragment = new CanceledFragment();
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
        View view =  inflater.inflate(R.layout.fragment_canceled, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerCanceled);
        loadReceivedOrder();






    }

    private void loadReceivedOrder() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Orders");
        orders = new ArrayList<>();
        mDatabase.orderByChild("orderStatus").equalTo("To Confirm").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {

                    Order order = orderSnapshot.getValue(Order.class);
                    orders.add(order);


                }
                Log.d("success", "sucessne");
                OrderAdapter orderAdapter = new OrderAdapter(orders, "Canceled");
                orderAdapter.setOnItemClickListener(new OrderAdapter.OrderAdapterListener() {
                    @Override
                    public void onItemClicked(Order order) {
                        Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                        intent.putExtra("order", order);
                        startActivity(intent);
                    }
                });
                Log.d("orders", String.valueOf(orders));
                recyclerView.setAdapter(orderAdapter);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}