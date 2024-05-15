package com.thaikimhuynh.miladyapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public interface CartItemRemovedListener {
        void onCartItemRemoved();
    }

    public CartAdapter(ArrayList<Product> carts, Context context, CartFragment cartFragment, ChangeNumberItemListener changeNumberItemsListener) {
        this.carts = carts;
        this.context = context;
        this.managementCart = new ManagementCart(context);
        this.cartFragment = cartFragment; // Initialize the reference to CartFragment
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
        holder.txtProductPrice.setText("$" + String.valueOf(Math.round(cart.getPrice())));
        holder.txtItemQuantity.setText(String.valueOf(cart.getNumberInCart()));
        holder.txtSize.setText(cart.getProductSize());
        holder.txtItemId.setText(cart.getGetItemId());
        holder.txtItemId.setVisibility(View.GONE);
        Glide.with(context).load(cart.getPicUrls().get(0)).transform(new CenterCrop()).into(holder.imvProduct);
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
        ImageView imvProduct,imgClear;
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

        }
    }
}
