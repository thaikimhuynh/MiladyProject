//package com.thaikimhuynh.miladyapp.product;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewpager.widget.ViewPager;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.thaikimhuynh.miladyapp.R;
//import com.thaikimhuynh.miladyapp.adapter.ViewPagerAdapter;
//import com.thaikimhuynh.miladyapp.databinding.ActivityProductDetailBinding;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import me.relex.circleindicator.CircleIndicator;
//
//
//public class ProductDetailActivity extends AppCompatActivity {
//    private ActivityProductDetailBinding activityProductDetailBinding;
//
//    Product product;
//    ViewPager viewPager;
//    CircleIndicator circleIndicator;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        activityProductDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
//        setContentView(activityProductDetailBinding.getRoot());
//
//        // Khởi tạo viewPager và circleIndicator
//        viewPager = findViewById(R.id.viewPagerProductImage);
//        circleIndicator = findViewById(R.id.circleIndicator);
//
//
//        Intent intent = getIntent();
//        Product product = (Product) intent.getSerializableExtra("product");
//
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("Items");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Lấy dữ liệu từ dataSnapshot và gán cho ViewPager và các view khác trong layout XML
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    product = snapshot.getValue(Product.class); // Gán dữ liệu vào product
//
//                    // Gán hình ảnh cho ViewPager
//                    List<String> imageUrls = new ArrayList<>();
//
//                    if (product != null && product.getPicUrl() != null) {
//                        for (Map.Entry<String, String> entry : product.getPicUrl().entrySet()) {
//                            imageUrls.add(entry.getValue());
//                        }
//                    }
//
//                    ViewPagerAdapter adapter = new ViewPagerAdapter(imageUrls, this);
//                    viewPager.setAdapter(adapter);
//                    circleIndicator.setViewPager(viewPager);
//
//                    // Sau khi có dữ liệu, bạn có thể gán giá trị cho các view trong layout XML tại đây
//                    // Ví dụ:
//                    // txtProductDetailName.setText(product.getTitle());
//                    // txtProductDetailPrice.setText("$" + product.getPrice());
//                    // Và tương tự cho các view khác
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra
//            }
//        });
//
//
//
//
//// Gán hình ảnh cho ViewPager
//        List<String> imageUrls = new ArrayList<>();
//
//        for (Map.Entry<String, String> entry : product.getPicUrl().entrySet()) {
//            imageUrls.add(entry.getValue());
//        }
//        ViewPagerAdapter adapter = new ViewPagerAdapter(imageUrls, this); // Thêm đối số context
//        viewPager.setAdapter(adapter);
//        circleIndicator.setViewPager(viewPager);
//
//    }
//
//
//
//}