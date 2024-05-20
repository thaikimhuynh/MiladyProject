//package com.thaikimhuynh.miladyapp.fragment;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.thaikimhuynh.miladyapp.R;
//import com.thaikimhuynh.miladyapp.adapter.OrderAdapter;
//import com.thaikimhuynh.miladyapp.checkout.ConfirmOrderUserActivity;
//import com.thaikimhuynh.miladyapp.model.Order;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ConfirmingFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class ConfirmingFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private OrderAdapter orderAdapter;
//    private List<Order> orderList;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public ConfirmingFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ConfirmingFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ConfirmingFragment newInstance(String param1, String param2) {
//        ConfirmingFragment fragment = new ConfirmingFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_confirming, container, false);
//
//        recyclerView = view.findViewById(R.id.recyclerViewOrders);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        orderList = new ArrayList<>();
//        orderAdapter = new OrderAdapter(orderList, order -> {
//            Intent intent = new Intent(getActivity(), ConfirmOrderUserActivity.class);
//            intent.putExtra("orderId", order.getOrderId());
//            startActivity(intent);
//        });
//        recyclerView.setAdapter(orderAdapter);
//
//        fetchOrders();
//
//        return view;
//    }
//
//    private void fetchOrders() {
//        String userId = getUserId(getContext());
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
//        databaseReference.orderByChild("orderStatus").equalTo("To Confirm")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        orderList.clear();
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Order order = snapshot.getValue(Order.class);
//                            if (order != null && order.getUserId().equals(userId)) {
//                                orderList.add(order);
//                            }
//                        }
//                        orderAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Handle possible errors.
//                    }
//                });
//    }
//
//    private String getUserId(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("user_session", MODE_PRIVATE);
//        return sharedPreferences.getString("user_id", "");
//    }
//}
