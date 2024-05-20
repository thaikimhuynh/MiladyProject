package com.thaikimhuynh.miladyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.List;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ProductOrderViewHolder> {
    private List<Product> productList;

    public ProductOrderAdapter(List<Product> productList) {
        this.productList = productList;
    }
    @NonNull
    @Override
    public ProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout, parent, false);
        return new ProductOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderViewHolder holder, int position) {
        Product product = productList.get(position);
        // Cập nhật giao diện cho item sản phẩm
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductOrderViewHolder extends RecyclerView.ViewHolder {
        // Khai báo các View tương ứng với item sản phẩm
        private TextView txtTitle;
        private TextView txtPrice;
        private ImageView imgProduct;

        public ProductOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các View từ layout item_product.xml
            txtTitle = itemView.findViewById(R.id.txtProductNameCheckout);
            txtPrice = itemView.findViewById(R.id.txtProductPriceCheckout);
            imgProduct = itemView.findViewById(R.id.imgProductCheckout);
        }

        public void bind(Product product) {
            // Cập nhật thông tin sản phẩm lên giao diện của item
            txtTitle.setText(product.getTitle());
            txtPrice.setText("$"+String.valueOf(product.getPrice()));

            // Sử dụng Glide hoặc các thư viện tương tự để load hình ảnh từ URL
            Glide.with(itemView.getContext()).load(product.getPicUrls().get(0)).into(imgProduct);
        }
    }
}
