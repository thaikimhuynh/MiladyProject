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
import com.thaikimhuynh.miladyapp.adapter.CouponAdapter;
import com.thaikimhuynh.miladyapp.adapter.PromotionAdapter;
import com.thaikimhuynh.miladyapp.model.Coupon;
import com.thaikimhuynh.miladyapp.model.RedeemPoints;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CouponFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    DatabaseReference database;
    CouponAdapter couponAdapter;
    ArrayList<Coupon> list;
    TextView textViewTotalCoupon;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CouponFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CouponFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CouponFragment newInstance(String param1, String param2) {
        CouponFragment fragment = new CouponFragment();
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_coupon, container, false);
        recyclerView = view.findViewById(R.id.coupon_recyclerView);
        textViewTotalCoupon = view.findViewById(R.id.textViewTotalCoupon);
        String userId = getUserId();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("VoucherWallet");
        Query query = reference.child(userId).child("voucherItems");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        couponAdapter = new CouponAdapter(getContext(), list);
        recyclerView.setAdapter(couponAdapter);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalCoupons = 0;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    String content = dataSnapshot.child("content").getValue(String.class);
//                    String title = dataSnapshot.child("title").getValue(String.class);
//                    String programname = dataSnapshot.child("programname").getValue(String.class);
                    // Tạo một đối tượng Coupon và đặt thông tin vào đó
                    Coupon coupon= dataSnapshot.getValue(Coupon.class);
                    list.add(coupon);
                    totalCoupons++;

                }
                textViewTotalCoupon.setText("Your Total Coupons \n " + totalCoupons);
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }
}