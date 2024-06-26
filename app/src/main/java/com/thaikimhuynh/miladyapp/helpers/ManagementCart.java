package com.thaikimhuynh.miladyapp.helpers;

import android.content.Context;

import androidx.annotation.NonNull;

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
    private void getNextItemId(DatabaseReference itemsRef, OnSuccessListener<Long> onSuccessListener) {
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long nextItemId = dataSnapshot.getChildrenCount();
                onSuccessListener.onSuccess(nextItemId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                onSuccessListener.onSuccess(null);
            }
        });
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
                // Handle error
            }
        });
    }
    private void addProductToCart(DatabaseReference userCartRef, Product item) {
        DatabaseReference itemsRef = userCartRef.child("items");
        getNextItemId(itemsRef, new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long itemId) {
                if (itemId != null) {
                    Map<String, Object> productDetails = new HashMap<>();
                    productDetails.put("title", item.getTitle());
                    productDetails.put("productID", item.getProductId());
                    productDetails.put("quantity", item.getNumberInCart());
                    productDetails.put("price", item.getPrice());
                    productDetails.put("size", item.getProductSize());
                    productDetails.put("pic", item.getPicUrls());
                    productDetails.put("itemId", item.getGetItemId());

                    itemsRef.child(String.valueOf(itemId)).setValue(productDetails);

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
                } else {
                }
            }
        });
    }

    private void createNewCartAndAddProduct(DatabaseReference userCartRef, Product item, String userId) {
        String cartId = generateCartId();

        Map<String, Object> cartDetails = new HashMap<>();
        cartDetails.put("userId", userId);
        userCartRef.setValue(cartDetails);

        DatabaseReference itemsRef = userCartRef.child("items");
        getNextItemId(itemsRef, new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long itemId) {
                if (itemId != null) {

                    Map<String, Object> productDetails = new HashMap<>();
                    productDetails.put("itemId", item.getGetItemId());
                    productDetails.put("productID", item.getProductId());
                    productDetails.put("title", item.getTitle());
                    productDetails.put("quantity", item.getNumberInCart());
                    productDetails.put("price", item.getPrice());
                    productDetails.put("size", item.getProductSize());
                    productDetails.put("pic", item.getPicUrls());

                    itemsRef.child(String.valueOf(itemId)).setValue(productDetails);

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

                    userCartRef.child("totalAmount").setValue(item.getNumberInCart());
                } else {

                }
            }
        });
    }
    private void updateCart(DatabaseReference userCartRef, Product item) {
        userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot cartSnapshot : dataSnapshot.child("items").getChildren()) {
                    String id = cartSnapshot.child("productID").getValue(String.class);
                    String size = cartSnapshot.child("size").getValue(String.class);
                    if (id != null && id.equals(item.getProductId()) && size != null && size.equals(item.getProductSize())) {
                        int quantity = cartSnapshot.child("quantity").getValue(Integer.class);
                        cartSnapshot.getRef().child("quantity").setValue(quantity + item.getNumberInCart());
                        found = true;

                        updateTotalAmount(userCartRef, item.getPrice() * item.getNumberInCart());

                        break;
                    }
                }
                if (!found) {
                    addProductToCart(userCartRef, item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateTotalAmount(DatabaseReference cartRef, double amountChange) {
        cartRef.child("totalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double totalAmount = dataSnapshot.getValue(Double.class);
                if (totalAmount == null) {
                    totalAmount = 0.0;
                }
                totalAmount += amountChange;
                if (totalAmount < 0) {
                    totalAmount = 0.0;
                }
                cartRef.child("totalAmount").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private String generateCartId() {
        String cartId = UUID.randomUUID().toString();
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

    public void removeProductFromCart(String itemId, String userId, ChangeNumberItemListener changeNumberItemListener) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double priceOfRemovedItem = 0;
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartUserId = cartSnapshot.child("userId").getValue(String.class);
                    if (cartUserId != null && cartUserId.equals(userId)) {
                        for (DataSnapshot productSnapshot : cartSnapshot.child("items").getChildren()) {
                            String itemID = productSnapshot.child("itemId").getValue(String.class);
                            double price = productSnapshot.child("price").getValue(Double.class);
                            if (itemID != null && itemID.equals(itemId)) {
                                priceOfRemovedItem = price;
                                productSnapshot.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            changeNumberItemListener.change();
                                            checkAndDeleteEmptyCart(cartSnapshot.getRef());
                                        });
                            }
                        }
                        subtractPriceFromTotalAmount(cartSnapshot.getRef(), priceOfRemovedItem);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



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
    private void subtractPriceFromTotalAmount(DatabaseReference cartRef, double price) {
        cartRef.child("totalAmount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double totalAmount = dataSnapshot.getValue(Double.class);
                if (totalAmount != null) { // Kiểm tra nếu totalAmount không null
                    totalAmount -= price;
                    if (totalAmount < 0) {
                        totalAmount = 0.0;
                    }
                    cartRef.child("totalAmount").setValue(totalAmount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void incrementProductQuantity(String itemId, String userId, ChangeNumberItemListener changeNumberItemListener) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartUserId = cartSnapshot.child("userId").getValue(String.class);
                    if (cartUserId != null && cartUserId.equals(userId)) {
                        for (DataSnapshot productSnapshot : cartSnapshot.child("items").getChildren()) {
                            String itemID = productSnapshot.child("itemId").getValue(String.class);
                            if (itemID != null && itemID.equals(itemId)) {
                                int quantity = productSnapshot.child("quantity").getValue(Integer.class);
                                double price = productSnapshot.child("price").getValue(Double.class);
                                // Tăng số lượng sản phẩm
                                productSnapshot.getRef().child("quantity").setValue(quantity + 1)
                                        .addOnSuccessListener(aVoid -> {
                                            updateTotalAmount(cartSnapshot.getRef(), +price);
                                            changeNumberItemListener.change();
                                        });
                                break;
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    public void decrementProductQuantity(String itemId, String userId, ChangeNumberItemListener changeNumberItemListener) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartUserId = cartSnapshot.child("userId").getValue(String.class);
                    if (cartUserId != null && cartUserId.equals(userId)) {
                        for (DataSnapshot productSnapshot : cartSnapshot.child("items").getChildren()) {
                            String itemID = productSnapshot.child("itemId").getValue(String.class);
                            if (itemID != null && itemID.equals(itemId)) {
                                int quantity = productSnapshot.child("quantity").getValue(Integer.class);
                                double price = productSnapshot.child("price").getValue(Double.class);
                                if (quantity > 0) {
                                    // Giảm số lượng sản phẩm
                                    productSnapshot.getRef().child("quantity").setValue(quantity - 1)
                                            .addOnSuccessListener(aVoid -> {
                                                updateTotalAmount(cartSnapshot.getRef(), -price);
                                                changeNumberItemListener.change();
                                            });
                                } else {
                                    productSnapshot.getRef().removeValue()
                                            .addOnSuccessListener(aVoid -> {
                                                updateTotalAmount(cartSnapshot.getRef(), -price);
                                                changeNumberItemListener.change();
                                                checkAndDeleteEmptyCart(cartSnapshot.getRef());
                                            });
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkAndDeleteEmptyCart(DatabaseReference cartRef) {
        cartRef.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    cartRef.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
