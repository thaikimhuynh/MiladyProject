package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Checkout;
import com.thaikimhuynh.miladyapp.model.Product;
import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    private Context context;
private List<Product> productList;
    public CheckoutAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtProductNameCheckout.setText(product.getTitle());
        holder.txtProductPriceCheckout.setText("$" + String.valueOf(product.getPrice()));
        holder.txtSize.setText(product.getProductSize());
        holder.txtProductCheckoutQuantity.setText("x" + String.valueOf(product.getNumberInCart()));
        if (product.getPicUrls() != null && !product.getPicUrls().isEmpty()) {
            Glide.with(context)
                    .load(product.getPicUrls().get(0))
                    .into(holder.imgProductCheckout);
        }
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductNameCheckout, txtProductPriceCheckout, txtSize, txtProductCheckoutQuantity;
        ImageView imgProductCheckout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductNameCheckout = itemView.findViewById(R.id.txtProductNameCheckout);
            txtProductPriceCheckout = itemView.findViewById(R.id.txtProductPriceCheckout);
            txtSize = itemView.findViewById(R.id.txtSize);
            txtProductCheckoutQuantity = itemView.findViewById(R.id.txtProductCheckoutQuantity);
            imgProductCheckout = itemView.findViewById(R.id.imgProductCheckout);
        }
    }
}
