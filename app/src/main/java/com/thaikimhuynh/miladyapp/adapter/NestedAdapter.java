package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.PaymentItem;
import com.thaikimhuynh.miladyapp.model.Product;
import com.thaikimhuynh.miladyapp.payment.WalletInformationActivity;
import com.thaikimhuynh.miladyapp.product.ProductDetailActivity;

import java.util.List;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.NestedViewHolder> {
    private List<PaymentItem> mList;

    public NestedAdapter(List<PaymentItem> mList){
        this.mList = mList;
    }

    @NonNull
    @Override
    public NestedAdapter.NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_payment_item, parent, false);
        return new NestedViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NestedAdapter.NestedViewHolder holder, int position) {

        PaymentItem paymentItem = mList.get(position);
        holder.phoneNumber.setText(paymentItem.getPhoneNumber());
        String imageUrl = paymentItem.getImg_logo();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.wallet_logo);
        Context context = holder.itemView.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentItem paymentItem = mList.get(position);
                Intent intent = new Intent(context, WalletInformationActivity.class);
                intent.putExtra("payment_info", paymentItem);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NestedViewHolder extends  RecyclerView.ViewHolder{
        private TextView nameEwallet, name, phoneNumber;
        private ImageView wallet_logo;


        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.tv_phonenumber);
            wallet_logo = itemView.findViewById(R.id.img_ewallet);
        }
    }
}
