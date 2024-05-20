package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.thaikimhuynh.miladyapp.R;

import java.util.List;

public class ProductDetailsSliderAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageUrls;

    public ProductDetailsSliderAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_productdetail_image, container, false);

        ImageView imageView = view.findViewById(R.id.imgProductDetail);

        // Kiểm tra xem danh sách hình ảnh có null không trước khi load ảnh
        if (imageUrls != null && position < imageUrls.size()) {
            Glide.with(context).load(imageUrls.get(position)).into(imageView);
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (imageUrls != null) {
            return imageUrls.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
