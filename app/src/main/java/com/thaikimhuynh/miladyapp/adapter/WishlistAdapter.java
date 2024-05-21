package com.thaikimhuynh.miladyapp.adapter;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Category;
import com.thaikimhuynh.miladyapp.model.Product;
import com.thaikimhuynh.miladyapp.product.ProductDetailActivity;

import java.util.ArrayList;
import java.util.Locale;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    private Context mContext;
    private ArrayList<Product> wishlist;


    private OnDeleteItemClickListener onDeleteItemClickListener;


    public WishlistAdapter(Context mContext, ArrayList<Product> wishlist) {
        this.mContext = mContext;
        this.wishlist = wishlist;


    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Product product = wishlist.get(position);

        if (product != null) {
            holder.wishlist_name.setText(product.getTitle());
            holder.wishlist_price.setText(String.format(Locale.US, "$%.1f", product.getPrice()));
            if (product.getPicUrls() != null && !product.getPicUrls().isEmpty()) {
                String imageUrl = product.getPicUrls().get(0);

                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .into(holder.wishlist_image);
            }
            Context context = holder.itemView.getContext();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                }
            });
            holder.bin_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteItemClickListener != null) {
                        onDeleteItemClickListener.onDeleteItemClick(product);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return wishlist != null ? wishlist.size() : 0;
    }

    static class WishlistViewHolder extends RecyclerView.ViewHolder {
        TextView wishlist_name,wishlist_price;
        ImageView wishlist_image,bin_image;


        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            wishlist_name = itemView.findViewById(R.id.txtWishlistName);
            wishlist_price = itemView.findViewById(R.id.txtWishlistPrice);
            wishlist_image = itemView.findViewById(R.id.imgWishlist);
            bin_image = itemView.findViewById(R.id.imgClear);
        }
    }

    public interface OnDeleteItemClickListener {
        void onDeleteItemClick(Product product);
    }

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener listener) {
        this.onDeleteItemClickListener = listener;
    }
    public void removeProduct(Product product) {
        int position = wishlist.indexOf(product);
        if (position != -1) {
            wishlist.remove(position);
            notifyItemRemoved(position);
        }
    }
}
