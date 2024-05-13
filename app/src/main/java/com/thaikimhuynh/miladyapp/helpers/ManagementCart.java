package com.thaikimhuynh.miladyapp.helpers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ManagementCart {
    private Context context;
    private DatabaseReference cartRef;

    public ManagementCart(Context context) {
        this.context = context;
        this.cartRef = FirebaseDatabase.getInstance().getReference().child("Cart");
    }
    public void insertProduct(Product item, String userId, int itemId) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartUserId = cartSnapshot.child("userId").getValue(String.class);
                    if (cartUserId != null && cartUserId.equals(userId)) {
                        DatabaseReference userCartRef = cartSnapshot.getRef();
                        updateCart(userCartRef, item);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    createNewCartAndAddProduct(cartRef.push(), item, userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateCart(DatabaseReference userCartRef, Product item) {
        userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot cartSnapshot : dataSnapshot.child("items").getChildren()) {
                    String title = cartSnapshot.child("title").getValue(String.class);
                    String size = cartSnapshot.child("size").getValue(String.class);
                    if (title != null && title.equals(item.getTitle()) && size != null && size.equals(item.getProductSize())) {
                        // Tìm thấy sản phẩm cùng tiêu đề và kích thước, tăng số lượng của sản phẩm
                        int quantity = cartSnapshot.child("quantity").getValue(Integer.class);
                        cartSnapshot.getRef().child("quantity").setValue(quantity + item.getNumberInCart());
                        found = true;

                        // Cập nhật tổng giá trị của giỏ hàng
                        updateTotalAmount(userCartRef, item.getPrice() * item.getNumberInCart());

                        break;
                    }
                }
                if (!found) {
                    // Không tìm thấy sản phẩm cùng tiêu đề và kích thước, thêm sản phẩm mới vào giỏ hàng
                    addProductToCart(userCartRef, item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý sự kiện onCancelled
            }
        });
    }

    private void updateTotalAmount(DatabaseReference userCartRef, double additionalAmount) {
        userCartRef.child("totalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalAmount = dataSnapshot.getValue(Double.class);
                totalAmount += additionalAmount;
                userCartRef.child("totalAmount").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý sự kiện onCancelled
            }
        });
    }


    private void addProductToCart(DatabaseReference userCartRef, Product item) {
        // Gán itemId cho sản phẩm

        // Tạo một HashMap để lưu thông tin sản phẩm
        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("title", item.getTitle());
        productDetails.put("quantity", item.getNumberInCart());
        productDetails.put("price", item.getPrice());
        productDetails.put("size", item.getProductSize());
        productDetails.put("pic", item.getPicUrls());
        productDetails.put("itemId", item.getGetItemId()); // Sử dụng itemId đã tạo cho sản phẩm
        userCartRef.child("items").push().setValue(productDetails);

        userCartRef.child("totalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalAmount = dataSnapshot.getValue(Double.class);
                totalAmount += (item.getNumberInCart() * item.getPrice());
                userCartRef.child("totalAmount").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void createNewCartAndAddProduct(DatabaseReference userCartRef, Product item, String userId) {
        String cartId = generateCartId();


        // Tạo một HashMap để lưu thông tin giỏ hàng mới
        Map<String, Object> cartDetails = new HashMap<>();
        cartDetails.put("userId", userId);

        // Tạo một HashMap để lưu thông tin sản phẩm
        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("itemId", item.getGetItemId());
        productDetails.put("title", item.getTitle());
        productDetails.put("quantity", item.getNumberInCart());
        productDetails.put("price", item.getPrice());
        productDetails.put("size", item.getProductSize());
        productDetails.put("pic", item.getPicUrls());
        userCartRef.child("userId").setValue(userId);
        userCartRef.child("items").push().setValue(productDetails);

        userCartRef.child("totalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalAmount = 0; // Khởi tạo tổng giá trị là 0
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    totalAmount += (product.getNumberInCart() * product.getPrice());
                }
                totalAmount += (item.getNumberInCart() * item.getPrice());
                userCartRef.child("totalAmount").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userCartRef.child("cartId").setValue(cartId);

        // Thêm total amount vào giỏ hàng mới
        userCartRef.child("totalAmount").setValue(item.getNumberInCart());
    }
    private String generateCartId() {
        // Tạo một chuỗi ngẫu nhiên
        String uuid = UUID.randomUUID().toString();
        // Lấy timestamp hiện tại và thêm vào chuỗi ngẫu nhiên
        String cartId = uuid + "_" + System.currentTimeMillis();
        return cartId;
    }


    public void getListCart(OnSuccessListener<ArrayList<Product>> onSuccessListener, String userID) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Product> productList = new ArrayList<>();
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartUserID = cartSnapshot.child("userId").getValue(String.class);
                    if (cartUserID != null && cartUserID.equals(userID)) {
                        for (DataSnapshot productSnapshot : cartSnapshot.child("items").getChildren()) {
                            String title = productSnapshot.child("title").getValue(String.class);
                            double price = productSnapshot.child("price").getValue(Double.class);
                            int quantity = productSnapshot.child("quantity").getValue(Integer.class);
                            String size = productSnapshot.child("size").getValue(String.class);
                            String itemId = productSnapshot.child("itemId").getValue(String.class);

                            List<String> picUrls = (List<String>) productSnapshot.child("pic").getValue();
                            Product product = new Product();
                            product.setTitle(title);
                            product.setPrice(price);
                            product.setProductSize(size);
                            product.setGetItemId(itemId);
                            product.setNumberInCart(quantity);
                            product.setPicUrls(picUrls);

                            productList.add(product);
                        }

                        onSuccessListener.onSuccess(productList);
                        return;
                    }
                }
                onSuccessListener.onSuccess(productList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    public void minusNumberItem(Product product, ChangeNumberItemListener changeNumberItemsListener) {
//        int newQuantity = product.getNumberInCart() - 1;
//        if (newQuantity <= 0) {
//            // Xoá sản phẩm khỏi giỏ hàng nếu số lượng là 0 hoặc âm
//            cartRef.child("items").child(product.getTitle()).removeValue()
//                    .addOnSuccessListener(aVoid -> changeNumberItemsListener.change());
//        } else {
//            // Cập nhật số lượng sản phẩm trên Realtime Database cho sản phẩm cụ thể
//            cartRef.child("items").child(product.getTitle()).child("quantity").setValue(newQuantity)
//                    .addOnSuccessListener(aVoid -> changeNumberItemsListener.change());
//        }
//    }

    public void plusNumberItem(String itemId, ChangeNumberItemListener changeNumberItemsListener) {
        DatabaseReference cartItemRef = cartRef.child("items").child(itemId).child("quantity");
        cartItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer currentQuantity = dataSnapshot.getValue(Integer.class);
                if (currentQuantity != null) {
                    int newQuantity = currentQuantity + 1;
                    cartItemRef.setValue(newQuantity).addOnSuccessListener(aVoid -> changeNumberItemsListener.change());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý sự kiện onCancelled nếu cần
            }
        });
    }


//    public void removeProductFromCart(String itemId, ChangeNumberItemListener changeNumberItemsListener) {
//        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
//                    for (DataSnapshot productSnapshot : cartSnapshot.child("items").getChildren()) {
//                        String productId = productSnapshot.child("itemId").getValue(String.class);
//                            productSnapshot.getRef().removeValue()
//                                    .addOnSuccessListener(onSuccessListener)
//                                    .addOnFailureListener(onFailureListener);
//                            return;
//                        }
//                    }
//                }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý sự kiện onCancelled
//                onFailureListener.onFailure(databaseError.toException());
//            }
//        });
//    }




    public void getTotal(String userID, OnSuccessListener<Double> onSuccessListener) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double total = 0;
                for (DataSnapshot userCartSnapshot : dataSnapshot.getChildren()) {
                    String cartUserID = userCartSnapshot.child("userId").getValue(String.class);
                    if (cartUserID != null && cartUserID.equals(userID)) {
                        DataSnapshot itemsSnapshot = userCartSnapshot.child("items");
                        for (DataSnapshot productSnapshot : itemsSnapshot.getChildren()) {
                            double price = productSnapshot.child("price").getValue(Double.class);
                            int quantity = productSnapshot.child("quantity").getValue(Integer.class);
                            total += price * quantity;
                        }
                    }
                }
                onSuccessListener.onSuccess(total);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





}