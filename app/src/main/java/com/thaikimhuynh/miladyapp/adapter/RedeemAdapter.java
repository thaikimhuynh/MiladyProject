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
import com.thaikimhuynh.miladyapp.model.RedeemPoints;

import java.util.ArrayList;

public class RedeemAdapter extends RecyclerView.Adapter<RedeemAdapter.MyViewHolder> {
    Context context;
    ArrayList<RedeemPoints> list;

    public RedeemAdapter(Context context, ArrayList<RedeemPoints> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_coupon_recyclerview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RedeemPoints redeemPoints=list.get(position);
        holder.title.setText(redeemPoints.getTitle());
        holder.content.setText(redeemPoints.getContent());
        holder.programname.setText(redeemPoints.getProgramname());
        holder.pointRequired.setText(redeemPoints.getPointRequired()+"P");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CouponDetailsActivity.class);
                intent.putExtra("title", redeemPoints.getTitle());
                intent.putExtra("description", redeemPoints.getDescription());
                intent.putExtra("voucherCode", redeemPoints.getVoucherCode());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, content, programname,pointRequired;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.textViewName);
            content= itemView.findViewById(R.id.textViewDescription);
            programname= itemView.findViewById(R.id.textViewProgramName);
            pointRequired= itemView.findViewById(R.id.txtPoints);


        }
    }
}
