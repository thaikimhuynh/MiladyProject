package com.thaikimhuynh.miladyapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.RedeemAdapter;
import com.thaikimhuynh.miladyapp.model.RedeemPoints;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RedeemPointsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RedeemPointsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    DatabaseReference database;
    RedeemAdapter redeemAdapter;
    ArrayList<RedeemPoints> list;
    TextView textViewTotalPoints;

    private String mParam1;
    private String mParam2;

    public RedeemPointsFragment() {
        // Required empty public constructor
    }

    public static RedeemPointsFragment newInstance(String param1, String param2) {
        RedeemPointsFragment fragment = new RedeemPointsFragment();
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
        View view = inflater.inflate(R.layout.fragment_redeem_points, container, false);
        recyclerView = view.findViewById(R.id.redeem_recyclerview);
        textViewTotalPoints = view.findViewById(R.id.textViewTotalPoints);
        database = FirebaseDatabase.getInstance().getReference("Voucher");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PointWallet");
        String userId = getUserId();
        Query query = reference.orderByChild("userId").equalTo(userId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        redeemAdapter = new RedeemAdapter(getContext(), list);
        recyclerView.setAdapter(redeemAdapter);

        // Add ValueEventListener to listen for changes in TotalPoints
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Object totalPointObj = dataSnapshot.child("totalPoint").getValue();
                        if (totalPointObj instanceof Long) {
                            Long totalPoints = (Long) totalPointObj;
                            textViewTotalPoints.setText("Your total points \n" + totalPoints);
                        } else if (totalPointObj instanceof String) {
                            String totalPoints = (String) totalPointObj;
                            textViewTotalPoints.setText("Your total points \n" + totalPoints);
                        } else {
                            textViewTotalPoints.setText("Your total points \n 0");
                        }
                    }
                } else {
                    textViewTotalPoints.setText("Your total points \n 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RedeemPoints redeemPoints = dataSnapshot.getValue(RedeemPoints.class);
                    list.add(redeemPoints);
                }
                redeemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });

        return view;
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }
}