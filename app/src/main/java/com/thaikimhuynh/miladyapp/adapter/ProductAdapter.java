package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.thaikimhuynh.miladyapp.ProductListActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Category;
import com.thaikimhuynh.miladyapp.model.Product;
import com.thaikimhuynh.miladyapp.product.ProductDetailActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mContext;
    private ArrayList<Product> productList;
    private String categoryTitle;

    public ProductAdapter(Context mContext, ArrayList<Product> productList, String categoryTitle) {
        this.mContext = mContext;
        this.productList = productList;
        this.categoryTitle = categoryTitle;
    }

    public ProductAdapter(Context mContext, ArrayList<Product> productList)
    {
        this.mContext = mContext;
        this.productList = productList;

    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position)
    {
        Product product = productList.get(position);

        holder.product_name.setText(product.getTitle());
//        holder.product_price.setText(String.valueOf(product.getPrice()));
        holder.product_price.setText(String.format(Locale.US, "$%.1f", product.getPrice()));

        String imageUrl = product.getPicUrls().get(0);

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.product_image);

        Context context = holder.itemView.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = productList.get(position);
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", product);
                intent.putExtra("categoryName", categoryTitle);
                context.startActivity(intent);
            }
        });




    }


    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productlist_item, parent, false);
        return new ProductAdapter.ProductViewHolder(view);
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, product_price;
        ImageView product_image;
        CardView cardLayout;

        public ProductViewHolder(@NonNull View itemView)
        {
            super(itemView);


            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_image = itemView.findViewById(R.id.product_image);
            cardLayout = itemView.findViewById(R.id.cardView_productList);
        }
    }
}
