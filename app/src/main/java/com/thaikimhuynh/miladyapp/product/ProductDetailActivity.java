    package com.thaikimhuynh.miladyapp.product;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.thaikimhuynh.miladyapp.R;
    import com.thaikimhuynh.miladyapp.adapter.ProductDetailsSliderAdapter;
    import com.thaikimhuynh.miladyapp.adapter.SizeAdapter;
    import com.thaikimhuynh.miladyapp.databinding.ActivityProductDetailBinding;
    import com.thaikimhuynh.miladyapp.fragment.CartFragment;
    import com.thaikimhuynh.miladyapp.model.Product;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.List;

    public class ProductDetailActivity extends AppCompatActivity {

        private ActivityProductDetailBinding productDetailBinding;
        private int quantity = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            productDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
            setContentView(productDetailBinding.getRoot());
            loadProductDetail();
            addEvents();
            loadCateName();
            initSize();
            editQuantity();


        }




        private void editQuantity() {
            productDetailBinding.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity > 1) { // Đảm bảo số lượng không nhỏ hơn 1
                        quantity--;
                        productDetailBinding.txtQuantity.setText(String.valueOf(quantity));
                    }
                }
            });

            productDetailBinding.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    productDetailBinding.txtQuantity.setText(String.valueOf(quantity));
                }
            });
        }

        private void initSize() {
            ArrayList<String> list = new ArrayList<>();
            list.add("36");
            list.add("37");
            list.add("38");
            list.add("39");
            list.add("40");

            productDetailBinding.listSize.setAdapter(new SizeAdapter(list));
            productDetailBinding.listSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        }

        private void loadCateName() {
            Product product = (Product) getIntent().getSerializableExtra("product");
            String categoryId = product.getCategoryId();

            DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category").child(categoryId);
            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String categoryName = dataSnapshot.child("name").getValue(String.class);

                        TextView productCategory = productDetailBinding.txtProductDetailCategory;
                        productCategory.setText(categoryName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        private void addEvents() {
            productDetailBinding.imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        private void loadProductDetail() {
            Product product = (Product) getIntent().getSerializableExtra("product");
            
            if (product != null) {
                String title = product.getTitle();
                String description = product.getDescription();
                double price = product.getPrice();
                List<String> picUrls = product.getPicUrls();

                TextView productName = productDetailBinding.txtProductDetailName;
                productName.setText(title);

                TextView productDescription = productDetailBinding.txtProductDetailDescription;
                productDescription.setText(description);

                TextView productPrice = productDetailBinding.txtProductDetailPrice;
                productPrice.setText("$" + price);


            // Load ảnh từ picUrls vào ViewPager
                List<ImageView> imageViewList = new ArrayList<>();
                for (String picUrl : picUrls) {
                    ImageView imageView = new ImageView(this);
                    imageViewList.add(imageView);
                }

                ProductDetailsSliderAdapter adapter = new ProductDetailsSliderAdapter(this, picUrls);
                productDetailBinding.viewPagerProductImage.setAdapter(adapter);
                productDetailBinding.circleIndicator.setViewPager(productDetailBinding.viewPagerProductImage);
            } else {
            }
        }

    }
