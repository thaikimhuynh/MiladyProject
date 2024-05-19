package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.CouponDetailsActivity;
import com.thaikimhuynh.miladyapp.Notification;
import com.thaikimhuynh.miladyapp.R;

import java.util.ArrayList;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {

    Context context;
    ArrayList<Notification> list;

    public PromotionAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_notification_promotion,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notification notification= list.get(position);
        holder.notification_title_promotion.setText(notification.getHead());
        holder.notification_content_promotion.setText(notification.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CouponDetailsActivity.class);
                intent.putExtra("title", notification.getHead());
                intent.putExtra("description", notification.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView notification_title_promotion,notification_content_promotion;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            notification_content_promotion = itemView.findViewById(R.id.notification_content_promotion);
            notification_title_promotion = itemView.findViewById(R.id.notification_title_promotion);


        }
    }

}
