package com.thaikimhuynh.miladyapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thaikimhuynh.miladyapp.adapter.CartAdapter;
import com.thaikimhuynh.miladyapp.checkout.CheckOutActivity;
import com.thaikimhuynh.miladyapp.databinding.FragmentCartBinding;
import com.thaikimhuynh.miladyapp.helpers.ChangeNumberItemListener;
import com.thaikimhuynh.miladyapp.helpers.ManagementCart;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types of parameters
    private FragmentCartBinding binding;
    private CartAdapter Cartadapter;
    private ManagementCart managementCart;
    public CartFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize ManagementCart
        managementCart = new ManagementCart(requireContext());

        // Load cart items
        loadCartItem();

        // Set event listeners
        setEventListeners();



        return view;
    }
    private void setEventListeners() {
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String userID = sharedPreferences.getString("userID", "");
                managementCart.getListCart(new OnSuccessListener<ArrayList<Product>>() {
                    @Override
                    public void onSuccess(ArrayList<Product> cartItems) {
                        Intent intent = new Intent(requireContext(), CheckOutActivity.class);
                        // Send the list of cart items via Intent
                        intent.putExtra("cart_items", cartItems);
                        startActivity(intent);
                    }
                }, userID); // Truyền giá trị userID vào phương thức getListCart()
            }
        });
    }


    private void loadCartItem() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerCart.setLayoutManager(layoutManager);


        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("userID", "");

        managementCart.getListCart(new OnSuccessListener<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> cartItems) {
                Cartadapter = new CartAdapter(cartItems, requireContext(), new ChangeNumberItemListener() {
                    @Override
                    public void change() {

                        calculateCart();
                    }
                });
                binding.recyclerCart.setAdapter(Cartadapter);


                calculateCart();
            }
        }, userID); // Pass userID to getListCart method
    }

    private void calculateCart() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("userID", "");

        managementCart.getTotal(userID, new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double total) {
                binding.txtCartPrice.setText("$" + String.valueOf(total));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
