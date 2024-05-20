package com.thaikimhuynh.miladyapp.checkout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.CartPaymentMethodActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.CartAdapter;
import com.thaikimhuynh.miladyapp.adapter.CheckoutAdapter;
import com.thaikimhuynh.miladyapp.databinding.ActivityCheckOutBinding;
import com.thaikimhuynh.miladyapp.fragment.CartFragment;
import com.thaikimhuynh.miladyapp.model.Address;
import com.thaikimhuynh.miladyapp.model.Checkout;
import com.thaikimhuynh.miladyapp.model.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.text.TextWatcher;

public class CheckOutActivity extends AppCompatActivity {
    RecyclerView recyclerViewCheckout;

    CheckoutAdapter checkOutAdapter;
    List<Product>cartProducts;
    double totalAmount;
    double finalAmount;

    double shippingFee = 20.0;
    private ActivityCheckOutBinding checkoutBinding;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkoutBinding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(checkoutBinding.getRoot());
        addEvents();

        addViews();
        loadCartItems();
        getDataFromAddressSelection();


    }


    private void getDataFromAddressSelection() {
        Intent intent = getIntent();
        if (intent != null) {
            String addressName = intent.getStringExtra("address_name");
            String addressAddress = intent.getStringExtra("address_address");
            String addressPhone = intent.getStringExtra("address_phone");

            // Cập nhật giao diện với dữ liệu nhận được
            TextView txtCusNameCheckout = findViewById(R.id.txtCusNameCheckout);
            TextView txtAddressCheckout = findViewById(R.id.txtAddressCheckout);
            TextView txtPhone = findViewById(R.id.txtPhone);

            txtCusNameCheckout.setText(addressName);
            txtAddressCheckout.setText(addressAddress);
            txtPhone.setText(addressPhone);
        }
    }

    private void loadCartItems() {
        String userId = getUserId();
        FirebaseDatabase.getInstance().getReference().child("Cart")
                .orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        cartProducts = new ArrayList<>();
                        totalAmount = 0.0;

                        for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                            if (cartSnapshot.child("totalAmount").exists()) {
                                totalAmount = cartSnapshot.child("totalAmount").getValue(Double.class);
                            }
                            if (!cartSnapshot.child("discountedAmount").exists()) {
                                cartSnapshot.getRef().child("discountedAmount").setValue(0.0);
                            }
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
                                cartProducts.add(product);
                            }
                        }
                        double finalAmount = totalAmount + shippingFee;
                        checkoutBinding.txtCheckoutPrice.setText("$" + totalAmount);
                        checkoutBinding.txtShippingFee.setText("$" + shippingFee);
                        checkoutBinding.txtTotalPriceCheckout.setText("$" + finalAmount);
                        checkoutBinding.txtVoucherCheckout.setText("$0.00");
                        updateFinalAmountInFirebase(finalAmount);
                        checkOutAdapter = new CheckoutAdapter(cartProducts, CheckOutActivity.this);
                        recyclerViewCheckout.setAdapter(checkOutAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void addViews() {
        recyclerViewCheckout=findViewById(R.id.listCheckout);
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));
    }



    private void addEvents() {
        checkoutBinding.btnContinuePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressName = checkoutBinding.txtCusNameCheckout.getText().toString();
                String addressAddress = checkoutBinding.txtAddressCheckout.getText().toString();
                String addressPhone = checkoutBinding.txtPhone.getText().toString();

                if (addressName.isEmpty() || addressAddress.isEmpty() || addressPhone.isEmpty()) {
                    Toast.makeText(CheckOutActivity.this, "Please select an address", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(CheckOutActivity.this, CartPaymentMethodActivity.class);

                intent.putExtra("address_name", addressName);
                intent.putExtra("address_address", addressAddress);
                intent.putExtra("address_phone", addressPhone);


                Log.d("CheckOutActivity", "address name: " + addressName);
                Log.d("CheckOutActivity", "address_address: " + addressAddress);
                Log.d("CheckOutActivity", "address phone: " + addressPhone);

                startActivity(intent);
            }
        });


        checkoutBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
finish();
            }
        });
        checkoutBinding.btnApplyVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyVoucher();
            }
        });
        checkoutBinding.edtVoucher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during text changes
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    updateTotalWithoutVoucher();
                }
            }


        });
    }

    private void updateTotalWithoutVoucher() {
        checkoutBinding.edtVoucher.setBackground(getResources().getDrawable(R.drawable.rounded_edittext_black_stroke));
        checkoutBinding.txtMessageError.setText("");
        double finalAmount = totalAmount + shippingFee;
        checkoutBinding.txtVoucherCheckout.setText("$0.00");
        checkoutBinding.txtTotalPriceCheckout.setText("$" + finalAmount);
        updateFinalAmountInFirebase(finalAmount);
resetVoucherDiscount();
    

    }

    private void resetVoucherDiscount() {
        String userId = getUserId();
        FirebaseDatabase.getInstance().getReference().child("Cart")
                .orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                            cartSnapshot.getRef().child("discountedAmount").setValue(0.0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    private void applyVoucher() {
        String edtVoucherCode = checkoutBinding.edtVoucher.getText().toString().trim().toUpperCase();
        Log.d("VoucherCode", "Voucher code entered: " + edtVoucherCode);
        String userId = getUserId();

        FirebaseDatabase.getInstance().getReference().child("VoucherWallet").child(userId).child("voucherItems")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot.toString());

                        boolean voucherFound = false;
                        double discountAmount = 0.0;

                        for (DataSnapshot voucherSnapshot : dataSnapshot.getChildren()) {
                            String voucherCode = voucherSnapshot.child("voucherCode").getValue(String.class);
                            if (voucherCode != null && voucherCode.equals(edtVoucherCode)) {
                                Log.d("VoucherCode", "Voucher code matched: " + voucherCode);
                                voucherFound = true;
                                Double discount = voucherSnapshot.child("discount").getValue(Double.class);

                                if (discount != null) {
                                    // Tính toán giá trị giảm giá
                                    double discountedAmount = totalAmount * discount;
                                    checkoutBinding.txtVoucherCheckout.setText("$" + discountedAmount);
                                    discountAmount = discountedAmount; // Cập nhật giá trị giảm giá

                                    Toast.makeText(CheckOutActivity.this, "Discount applied successfully", Toast.LENGTH_SHORT).show();

                                //Save voucherDiscount on firebase
                                updateVoucherDiscount(discountedAmount);
                                    break; // Kết thúc khi tìm thấy voucher
                                }
                            }
                        }

                        if (!voucherFound) {
                            checkoutBinding.edtVoucher.setBackground(getResources().getDrawable(R.drawable.rounded_edittext_red_stroke));
                            checkoutBinding.txtMessageError.setText("Invalid Voucher Code");
                            discountAmount = 0.0;

                        }

                        // Tính tổng khi không áp dụng voucher
//                        checkoutBinding.txtShippingFee.setText("$" + shippingFee);

                        if (voucherFound) {
                            // Tính tổng khi áp dụng voucher
                            finalAmount = totalAmount - discountAmount + shippingFee;
                        } else {
                            // Tính tổng khi không áp dụng voucher
                            finalAmount = totalAmount + shippingFee;
                        }

                        checkoutBinding.txtTotalPriceCheckout.setText("$" + finalAmount);

                        updateFinalAmountInFirebase(finalAmount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("VoucherCode", "Firebase onCancelled called with error: " + databaseError.toString());
                        Toast.makeText(CheckOutActivity.this, "Error applying voucher", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateVoucherDiscount(double discountedAmount) {
        String userId = getUserId();
        FirebaseDatabase.getInstance().getReference().child("Cart")
                .orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                            // Cập nhật finalAmount
                            cartSnapshot.getRef().child("discountedAmount").setValue(discountedAmount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    private void updateFinalAmountInFirebase(double finalAmount) {
        String userId = getUserId();
        FirebaseDatabase.getInstance().getReference().child("Cart")
                .orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                            // Cập nhật finalAmount
                            cartSnapshot.getRef().child("finalAmount").setValue(finalAmount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }


    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");
        Log.d("VoucherCode", "getUserId() returned: " + userId);
        return userId;
    }


    private boolean isVoucherValid(String expirationDate) {
        // Implement a method to check if the voucher expiration date is valid
        // For simplicity, you can use SimpleDateFormat to parse the date and compare it with the current date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date expiry = sdf.parse(expirationDate);
            return new Date().before(expiry);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void openAddressSelectedActivity(View view) {
            Intent intent = new Intent(CheckOutActivity.this, AddressSelectionActivity.class);
            startActivityForResult(intent, 1); // Use requestCode 1
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Address selectedAddress = (Address) data.getSerializableExtra("selectedAddress");
            if (selectedAddress != null) {
                checkoutBinding.txtCusNameCheckout.setText(selectedAddress.getName());
                checkoutBinding.txtAddressCheckout.setText(selectedAddress.getAddress());
                checkoutBinding.txtPhone.setText(selectedAddress.getPhone());

                // Save selected address to shared preferences or any persistent storage for later use
                saveSelectedAddress(selectedAddress);
            }
        }
    }

    private void saveSelectedAddress(Address address) {
        SharedPreferences sharedPreferences = getSharedPreferences("selected_address", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", address.getName());
        editor.putString("address", address.getAddress());
        editor.putString("phone", address.getPhone());
        editor.apply();
    }

}