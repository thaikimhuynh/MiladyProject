package com.thaikimhuynh.miladyapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.fragment.CartFragment;
import com.thaikimhuynh.miladyapp.helpers.ChangeNumberItemListener;
import com.thaikimhuynh.miladyapp.helpers.ManagementCart;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> carts;
    private ManagementCart managementCart;
    private CartFragment cartFragment;
    private ChangeNumberItemListener changeNumberItemsListener;


    public CartAdapter(ArrayList<Product> carts, Context context, CartFragment cartFragment, ChangeNumberItemListener changeNumberItemsListener) {
        this.carts = carts;
        this.context = context;
        this.managementCart = new ManagementCart(context);
        this.cartFragment = cartFragment; // Initialize the reference to CartFragment
        this.changeNumberItemsListener = changeNumberItemsListener;
    }
    public CartAdapter(ArrayList<Product> carts, Context context) {
        this.carts = carts;
        this.context = context;
        this.managementCart = new ManagementCart(context);
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
        holder.txtProductPrice.setText("$" + String.valueOf(Math.round(cart.getPrice())));
        holder.txtItemQuantity.setText("x" +String.valueOf(cart.getNumberInCart()));
        holder.txtSize.setText(cart.getProductSize());
        holder.txtItemId.setText(cart.getGetItemId());
        holder.txtItemId.setVisibility(View.GONE);
        Glide.with(context).load(cart.getPicUrls().get(0)).transform(new CenterCrop()).into(holder.imvProduct);
        holder.imgPlus.setOnClickListener(v -> {
            String userId = getUserId();
            managementCart.incrementProductQuantity(itemId, userId, () -> {
                cart.setNumberInCart(cart.getNumberInCart() + 1);
                holder.txtItemQuantity.setText("x" + String.valueOf(cart.getNumberInCart()));
                if (cartFragment != null) {
                    cartFragment.updateCart();
                }
                Toast.makeText(context, "Quantity increased", Toast.LENGTH_SHORT).show();
            });
        });
        holder.imgMinus.setOnClickListener(v -> {
            String userId = getUserId();
            managementCart.decrementProductQuantity(itemId, userId, () -> {
                int currentQuantity = cart.getNumberInCart() - 1;
                if (currentQuantity > 0) {
                    cart.setNumberInCart(currentQuantity);
                    holder.txtItemQuantity.setText("x" + currentQuantity);
                    if (cartFragment != null) {
                        cartFragment.updateCart();
                    }
                    Toast.makeText(context, "Quantity decreased", Toast.LENGTH_SHORT).show();
                } else {
                    managementCart.removeProductFromCart(itemId, userId, () -> {
                        removeItem(itemId);
                        notifyDataSetChanged();
                        if (cartFragment != null) {
                            cartFragment.updateCart();
                        }
                        Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

        holder.imgClear.setOnClickListener(v -> {
            String userId = getUserId();
            managementCart.removeProductFromCart(itemId, userId, () -> {
                removeItem(itemId);
                notifyDataSetChanged();
                if (cartFragment != null) {
                    cartFragment.updateCart();
                }
                Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();

            });
        });
    }



    private String getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }


    public void removeItem(String itemId) {
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i).getGetItemId().equals(itemId)) {
                carts.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvProduct,imgClear,imgPlus,imgMinus;
        TextView txtProductName, txtProductPrice, txtItemQuantity, txtSize, txtItemId;

        public ViewHolder(View view) {
            super(view);
            imvProduct = view.findViewById(R.id.imgProductCart);
            txtProductName = view.findViewById(R.id.txtProductNameCart);
            txtProductPrice = view.findViewById(R.id.txtProductPriceCart);
            txtSize = view.findViewById(R.id.txtSize);
            txtItemQuantity = view.findViewById(R.id.txtQuantity);
            imgClear = view.findViewById(R.id.imgClear);
            txtItemId = view.findViewById(R.id.txtitemID);
            imgPlus = view.findViewById(R.id.btnMinus);
            imgMinus = view.findViewById(R.id.btnPlus);
        }
    }
}
