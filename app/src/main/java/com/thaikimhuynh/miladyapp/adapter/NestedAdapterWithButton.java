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

public class NestedAdapterWithButton extends RecyclerView.Adapter<NestedAdapterWithButton.NestedViewHolder> {
    private List<PaymentItem> mList;
    private int selectedPosition = -1;
    private List<NestedAdapterWithButton> nestedAdapters;

    public NestedAdapterWithButton(List<PaymentItem> mList){
        this.mList = mList;
    }

    @NonNull
    @Override
    public NestedAdapterWithButton.NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_payment_item_card, parent, false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedAdapterWithButton.NestedViewHolder holder, int position) {
        PaymentItem paymentItem = mList.get(position);
        holder.phoneNumber.setText(paymentItem.getPhoneNumber());
        String imageUrl = paymentItem.getImg_logo();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.wallet_logo);

        // Check if this item is selected or not
        if (selectedPosition == position) {
            holder.selectButton.setImageResource(R.mipmap.selected_button);
        } else {
            holder.selectButton.setImageResource(R.mipmap.select_button);
        }

        holder.selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != position) {
                    // If another button is selected, deselect it
                    int previouslySelectedPosition = selectedPosition;
                    selectedPosition = position;
                    notifyAllAdapters(previouslySelectedPosition);
                } else {
                    // If the same button is clicked again, deselect it
                    selectedPosition = -1;
                    notifyAllAdapters(position);
                }
            }
        });
    }

    private void notifyAllAdapters(int position) {
        for (NestedAdapterWithButton adapter : nestedAdapters) {
            adapter.setSelectedPosition(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder{
        private TextView phoneNumber;
        private ImageView wallet_logo, selectButton;

        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.tv_phonenumber_cart);
            wallet_logo = itemView.findViewById(R.id.img_ewallet_cart);
            selectButton = itemView.findViewById(R.id.select_button_cart);
        }
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
}
