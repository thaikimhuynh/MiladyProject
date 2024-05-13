package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.helpers.ChangeNumberItemListener;
import com.thaikimhuynh.miladyapp.helpers.ManagementCart;
import com.thaikimhuynh.miladyapp.model.Cart;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> carts;
    private ManagementCart managementCart;
    private DatabaseReference userCartRef;

    private ChangeNumberItemListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Product> carts, Context context, ChangeNumberItemListener changeNumberItemsListener) {
        this.carts = carts;
        this.context = context;
        this.managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item_fragment_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product cart = carts.get(position);
        String itemId = cart.getGetItemId();
        holder.txtProductName.setText(cart.getTitle());
        holder.txtProductPrice.setText("$"+String.valueOf(Math.round(cart.getPrice())) );
        holder.txtItemQuantity.setText(String.valueOf(cart.getNumberInCart()));
        holder.txtSize.setText(cart.getProductSize());
        holder.txtItemId.setText(cart.getGetItemId());
        Glide.with(context).load(cart.getPicUrls().get(0)).transform(new CenterCrop()).into(holder.imvProduct);
//        holder.btnClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String itemId = cart.getGetItemId();
//                ManagementCart managementCart = new ManagementCart(context);
//                managementCart.removeProductFromCart(itemId, new ChangeNumberItemListener() {
//                    @Override
//                    public void change() {
//                        notifyDataSetChanged();
//                    }
//                });
//            }
//        });


        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = carts.get(position).getGetItemId(); // Lấy itemId của sản phẩm tại vị trí hiện tại
                Log.d("ItemIdCheck", "ItemId at position " + position + ": " + itemId);
                managementCart.plusNumberItem(itemId, new ChangeNumberItemListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvProduct,imgPlus,imgMinus;
        Button btnClear;
        TextView txtProductName, txtProductPrice, txtItemQuantity,txtSize,txtItemId;

        public ViewHolder(View view) {
            super(view);
            imvProduct = view.findViewById(R.id.imgProductCart);
            txtProductName = view.findViewById(R.id.txtProductNameCart);
            txtProductPrice = view.findViewById(R.id.txtProductPriceCart);
            txtSize =  view.findViewById(R.id.txtSize);
            txtItemQuantity = view.findViewById(R.id.txtQuantity);
            btnClear=view.findViewById(R.id.btnClear);
            txtItemId = view.findViewById(R.id.txtitemID);
            imgPlus = view.findViewById(R.id.imgPlus);
            imgMinus = view.findViewById(R.id.imgMinus);
        }
    }
}

