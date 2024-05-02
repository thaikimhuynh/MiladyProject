package com.thaikimhuynh.miladyapp.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.adapter.ProductImageAdapter;
import com.thaikimhuynh.miladyapp.model.ProductImage;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class ProductDetailActivity extends AppCompatActivity {
ViewPager viewPagerProductImage;
CircleIndicator circleIndicator;
List<ProductImage>listProductImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        addViews();
    }

    private void addViews() {
        viewPagerProductImage=findViewById(R.id.viewPagerProductImage);
        circleIndicator=findViewById(R.id.circleIndicator);

        listProductImage = getListImage();
        ProductImageAdapter adapter=new ProductImageAdapter(listProductImage);
        viewPagerProductImage.setAdapter(adapter);

        circleIndicator.setViewPager(viewPagerProductImage);
    }
    private List<ProductImage>getListImage(){
        List<ProductImage> list = new ArrayList<>();
        list.add(new ProductImage(R.mipmap.ic_cross_border_sandals_3));
        list.add(new ProductImage(R.mipmap.ic_remove_bgr));
        list.add(new ProductImage(R.mipmap.ic_cross_border_sandals_1));

        return list;
    }
}