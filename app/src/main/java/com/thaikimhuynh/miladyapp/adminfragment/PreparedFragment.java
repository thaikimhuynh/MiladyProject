package com.thaikimhuynh.miladyapp.adminfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.AdminOrderAdapter;
import com.thaikimhuynh.miladyapp.model.AdminOrder;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreparedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreparedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    DatabaseReference database,databse2;
    AdminOrderAdapter adminOrderAdapter;
    ArrayList<AdminOrder> list;

    public PreparedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreparedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreparedFragment newInstance(String param1, String param2) {
        PreparedFragment fragment = new PreparedFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prepared, container, false);
//        recyclerView = view.findViewById(R.id.Preparing_RecyclerView);
        database = FirebaseDatabase.getInstance().getReference("Orders");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adminOrderAdapter = new AdminOrderAdapter(getContext(), list);
        recyclerView.setAdapter(adminOrderAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String orderStatus = dataSnapshot.child("orderStatus").getValue(String.class);
                    if ("Preparing".equals(orderStatus)){
                        AdminOrder adminOrder = dataSnapshot.getValue(AdminOrder.class);
                        list.add(adminOrder);
                    }
                    adminOrderAdapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        // Inflate the layout for this fragment
        return view;
        // Inflate the layout for this fragment
    }
}