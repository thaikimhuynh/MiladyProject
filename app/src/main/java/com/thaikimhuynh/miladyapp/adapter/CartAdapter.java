package com.thaikimhuynh.miladyapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> cartList;

    public CartAdapter(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView;
        TextView productPriceTextView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.txtProducttNameCart);
            productPriceTextView = itemView.findViewById(R.id.txtProductPriceCart);
        }

        public void bind(Cart cart) {
            productNameTextView.setText(cart.getProductName());
            productPriceTextView.setText(String.valueOf(cart.getProductPrice()));
        }
    }
}
