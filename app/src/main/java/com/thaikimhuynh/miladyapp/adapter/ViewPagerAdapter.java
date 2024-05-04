package com.thaikimhuynh.miladyapp.adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thaikimhuynh.miladyapp.R;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<String> imageUrls;
    private LayoutInflater inflater;
    private Context context;
    private FirebaseStorage storage;

    public ViewPagerAdapter(List<String> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_productdetail_image, container, false);

        final ImageView imageView = itemView.findViewById(R.id.imgProductDetail);

        // Tạo một reference đến hình ảnh trong Firebase Storage
        StorageReference imageRef = storage.getReferenceFromUrl(imageUrls.get(position));

        // Tải hình ảnh từ Firebase Storage và hiển thị trong ImageView
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Chuyển đổi byte array thành bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Hiển thị hình ảnh trong ImageView
                imageView.setImageBitmap(bitmap);
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
