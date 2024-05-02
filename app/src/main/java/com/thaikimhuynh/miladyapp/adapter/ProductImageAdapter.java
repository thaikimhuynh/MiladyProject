package com.thaikimhuynh.miladyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import androidx.viewpager.widget.PagerAdapter;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.ProductImage;

import java.util.List;

public class ProductImageAdapter extends PagerAdapter {
private List<ProductImage> listProductImage;

    public ProductImageAdapter(List<ProductImage> listProductImage) {
        this.listProductImage = listProductImage;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_productdetail_image, container,false );
        ImageView imgProductImage=view.findViewById(R.id.imgProductDetail);

        ProductImage proImage=listProductImage.get(position);
        imgProductImage.setImageResource(proImage.getResourceId());
        //add view
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (listProductImage != null){
            return listProductImage.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==object;
    }
}
