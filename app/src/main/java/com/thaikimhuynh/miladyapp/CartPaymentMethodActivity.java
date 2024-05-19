package com.thaikimhuynh.miladyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.adapter.ItemWithButtonAdapter;
import com.thaikimhuynh.miladyapp.checkout.PlaceOrderSuccessfullyActivity;
import com.thaikimhuynh.miladyapp.model.Order;
import com.thaikimhuynh.miladyapp.model.PaymentGroup;
import com.thaikimhuynh.miladyapp.model.PaymentItem;
import com.thaikimhuynh.miladyapp.model.Product;
import com.thaikimhuynh.miladyapp.model.SharedViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CartPaymentMethodActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PaymentGroup> mList;
    private ItemWithButtonAdapter itemAdapter;
    DatabaseReference mbase_1, mbase_2;

    private String addressName;
    private String addressAddress;
    private String addressPhone;
    String paymentMethod;
    Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_payment_method);
        addViews();

        Intent intent = getIntent();
        if (intent != null) {
          addressName = intent.getStringExtra("address_name");
           addressAddress = intent.getStringExtra("address_address");
           addressPhone = intent.getStringExtra("address_phone");}

        String userId = getUserId();
        getUserPaymentMethod();
        mbase_2 = FirebaseDatabase.getInstance().getReference("PaymentMethod");
        mbase_1 = FirebaseDatabase.getInstance().getReference("PaymentAccount");
        mList = new ArrayList<>();
        itemAdapter = new ItemWithButtonAdapter(this, mList);
        recyclerView.setAdapter(itemAdapter);
        loadPaymentMethod(userId);
        checkAndAddMissingGroup();
        addEvents();

        Log.d("size m list", String.valueOf(itemAdapter.getItemCount()));
    }

    private void getUserPaymentMethod() {
        SharedViewModel sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getSharedVariable().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String data) {

                paymentMethod = data;


            }
        });

    }

    private void addEvents() {
        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentMethod != null){
                    transferCartToOrders();
                }
                else
                {
                    Toast.makeText(CartPaymentMethodActivity.this, "Please choose a payment method", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }






    private void transferCartToOrders() {
        String userId = getUserId();

        Query cartQuery = FirebaseDatabase.getInstance().getReference("Cart").orderByChild("userId").equalTo(userId);
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");


        cartQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("cartSnapshot", dataSnapshot.getChildren().toString());

                    for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                        Order order = new Order();
                        Log.d("order.text", "complete");

                        order.setUserId(userId);

                        // Retrieve and set totalAmount, finalAmount, and discountedAmount
                        double totalAmount = cartSnapshot.child("totalAmount").getValue(Double.class);
                        double finalAmount = cartSnapshot.child("finalAmount").getValue(Double.class);
                        double discountedAmount = cartSnapshot.child("discountedAmount").getValue(Double.class);

                        order.setTotalAmount(totalAmount);
                        order.setFinalAmount(finalAmount);
                        order.setDiscountedAmount(discountedAmount);

                        List<Product> products = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : cartSnapshot.child("items").getChildren()) {
                            Product product = new Product();
                            product.setTitle(itemSnapshot.child("title").getValue(String.class));
                            product.setPrice(itemSnapshot.child("price").getValue(Double.class));
                            product.setNumberInCart(itemSnapshot.child("quantity").getValue(Integer.class));
                            product.setProductSize(itemSnapshot.child("size").getValue(String.class));

                            List<String> picUrls = new ArrayList<>();
                            for (DataSnapshot picSnapshot : itemSnapshot.child("pic").getChildren()) {
                                picUrls.add(picSnapshot.getValue(String.class));
                            }
                            product.setPicUrls(picUrls);
                            products.add(product);
                        }
                        order.setProducts(products);

                        order.setCustomerName(addressName);
                        order.setAddress(addressAddress);
                        order.setPhone(addressPhone);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String currentDate = dateFormat.format(new Date());
                        order.setOrderDate(currentDate);
                        order.setOrderStatus("To Confirm");
                        order.setPaymentMethod(paymentMethod);
                        Log.d("order.text", order.toString());


                        // Generate and check for a unique 4-digit order ID
                        generateUniqueOrderId(ordersRef, new OrderIdCallback() {
                            @Override
                            public void onOrderIdGenerated(int orderId) {
                                order.setOrderId(orderId);
                                Log.d("order.text", order.toString());
                                ordersRef.child(String.valueOf(orderId)).setValue(order);

                                // Clear the cart after order is placed
                                cartSnapshot.getRef().removeValue();

                                Toast.makeText(CartPaymentMethodActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CartPaymentMethodActivity.this, PlaceOrderSuccessfullyActivity.class);
                                intent.putExtra("order", order);

                                startActivity(intent);
//                                clearCart();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error transferring cart to orders", databaseError.toException());
                Toast.makeText(CartPaymentMethodActivity.this, "Error placing order", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateUniqueOrderId(DatabaseReference ordersRef, OrderIdCallback callback) {
        Random random = new Random();
        int orderId = 1000 + random.nextInt(9000); // Generate a 4-digit number

        ordersRef.child(String.valueOf(orderId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // If order ID already exists, generate a new one
                    generateUniqueOrderId(ordersRef, callback);
                } else {
                    // Order ID is unique, use it
                    callback.onOrderIdGenerated(orderId);
                    Log.d("orderid", String.valueOf(orderId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error checking for unique order ID", databaseError.toException());
            }
        });
    }

//    private void generateUniqueOrderId(DatabaseReference ordersRef, OrderIdCallback callback) {
//        Random random = new Random();
//        int orderId = 1000 + random.nextInt(9000); // Generate a 4-digit number
//
//        // Tạo một node con mới trong cơ sở dữ liệu Firebase
//        DatabaseReference newOrderRef = ordersRef.push();
//
//        // Lấy key của node con mới, đó chính là orderId
//
//        String orderIdKey = newOrderRef.getKey();
//
//        // Sử dụng orderId được tạo để gọi callback
//        callback.onOrderIdGenerated(orderIdKey);
//    }

    private interface OrderIdCallback {
        void onOrderIdGenerated(int orderId);
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void loadPaymentMethod(String userId) {
        mbase_1.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("result", String.valueOf(dataSnapshot.exists()));
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String paymentMethodId = snapshot.child("paymentMethodId").getValue(String.class);

                        mbase_2.orderByChild("id").equalTo(paymentMethodId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot paymentMethodSnapshot) {
                                for (DataSnapshot methodSnapshot : paymentMethodSnapshot.getChildren()) {
                                    String paymentMethodId = snapshot.child("paymentId").toString();
                                    String eWalletName = methodSnapshot.child("type").getValue(String.class);
                                    String imgLogo = methodSnapshot.child("logo").getValue(String.class);
                                    Log.d("imgLogo", "imgLogo: " + imgLogo);

                                    String userName = snapshot.child("name").getValue(String.class);
                                    String userAccountNumber = snapshot.child("accountNumber").getValue(String.class);
                                    String layout = eWalletName.equals("Momo") ? "1" : "2";

                                    PaymentItem paymentItem = new PaymentItem(paymentMethodId, eWalletName, userName, userAccountNumber, imgLogo, layout);
                                    Log.d("Payment Item tren", paymentItem.toString());

                                    addItemToGroup(paymentItem);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError paymentMethodError) {
                                // Handle error
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void checkAndAddMissingGroup() {
        boolean foundEWalletGroup = false;
        boolean foundBankingGroup = false;

        for (PaymentGroup group : mList) {
            if (group.getItemText().equalsIgnoreCase("E-wallet")) {
                foundEWalletGroup = true;
            } else if (group.getItemText().equalsIgnoreCase("Banking")) {
                foundBankingGroup = true;
            }
        }

        if (!foundEWalletGroup) {
            Log.d("Khongofound", "ko found");
            // Add a new group for E-wallet
            mList.add(new PaymentGroup(new ArrayList<>(), "E-wallet"));
            itemAdapter.notifyDataSetChanged();
            Log.d("itclannay", String.valueOf(mList.size()));
        }

        if (!foundBankingGroup) {
            // Add a new group for Banking
            mList.add(new PaymentGroup(new ArrayList<>(), "Banking"));
            itemAdapter.notifyDataSetChanged();
            Log.d("itclannay", String.valueOf(itemAdapter.getItemCount()));
        }
        mList.add(new PaymentGroup(null, "COD"));

        // Notify the adapter about the changes
        if (itemAdapter != null) {
            itemAdapter.notifyDataSetChanged();
        } else {
            itemAdapter = new ItemWithButtonAdapter(this, mList);
            recyclerView.setAdapter(itemAdapter);
            itemAdapter.notifyDataSetChanged();
        }
    }

    private void addItemToGroup(PaymentItem paymentItem) {
        boolean foundGroup = false;

        for (PaymentGroup group : mList) {
            Log.d("Payment Item group", paymentItem.toString());

            // Check if the group name matches the payment method type
            if ((isEwallet(paymentItem) && group.getItemText().equalsIgnoreCase("E-wallet")) ||
                    (!isEwallet(paymentItem) && group.getItemText().equalsIgnoreCase("Banking"))) {
                group.getItemList().add(paymentItem);
                foundGroup = true;
                break; // Break after adding the item to the group
            }
        }

        if (!foundGroup) {
            // Create a new group
            Log.d("Payment Item", paymentItem.toString());
            List<PaymentItem> items = new ArrayList<>();
            items.add(paymentItem);
            String groupName = isEwallet(paymentItem) ? "E-wallet" : "Banking";
            mList.add(new PaymentGroup(items, groupName));
        }
        Log.d("mList", "Loaded items: " + mList);

        // Update the RecyclerView adapter
        if (itemAdapter == null) {
            itemAdapter = new ItemWithButtonAdapter(CartPaymentMethodActivity.this, mList);
            recyclerView.setAdapter(itemAdapter);
        } else {
            itemAdapter.notifyDataSetChanged();
        }
    }

    private boolean isEwallet(PaymentItem paymentItem) {
        String paymentMethodName = paymentItem.getWallet_name().toLowerCase();
        return paymentMethodName.equals("momo") || paymentMethodName.equals("zalo pay");
    }

    private void addViews() {
        recyclerView = findViewById(R.id.rcv_mywallet_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
    }
}
