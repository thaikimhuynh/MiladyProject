package com.thaikimhuynh.miladyapp.adapter;

import android.annotation.SuppressLint;
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
import com.thaikimhuynh.miladyapp.model.Yours;

import java.util.ArrayList;

public class YoursAdapter extends RecyclerView.Adapter<YoursAdapter.MyViewHolder> {
    Context context;
    ArrayList<Yours> list;
    public YoursAdapter(Context context, ArrayList<Yours> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_notification_yours,parent,false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Yours yours= list.get(position);
        holder.notification_title_yours.setText("Reminder of order \n"+ yours.getOrderId() +" on "+ yours.getOrderDate());
        holder.notification_content_yours.setText("Your order on "+ yours.getOrderDate()+" is at the status: "+ yours.getOrderStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView notification_title_yours,notification_content_yours;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notification_content_yours= itemView.findViewById(R.id.notification_content_yours);
            notification_title_yours = itemView.findViewById(R.id.notification_title_yours);
        }
    }
}
