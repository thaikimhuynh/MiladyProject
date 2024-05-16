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
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Coupon;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {
    Context context;
    ArrayList<Coupon> list;

    public CouponAdapter(Context context, ArrayList<Coupon> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_redeempoint_recyclerview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Coupon coupon= list.get(position);
        holder.textViewDescription.setText(coupon.getContent());
        holder.textViewName.setText(coupon.getTitle());
        holder.textViewProgramName.setText(coupon.getProgramname());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CouponDetailsActivity.class);
                intent.putExtra("title", coupon.getTitle());
                intent.putExtra("description", coupon.getDescription());
                intent.putExtra("voucherCode", coupon.getVoucherCode());
                context.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName,textViewDescription, textViewProgramName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewProgramName = itemView.findViewById(R.id.textViewProgramName);


        }
    }

}
