package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.admin.AdminConfirmOrderActivity;
import com.thaikimhuynh.miladyapp.admin.AdminDeliverySuccessfullyActivity;
import com.thaikimhuynh.miladyapp.admin.AdminOnDeliveryActivity;
import com.thaikimhuynh.miladyapp.admin.AdminPrepareOrderActivity;
import com.thaikimhuynh.miladyapp.model.AdminOrder;

import java.util.ArrayList;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<AdminOrder> list;
    public AdminOrderAdapter(Context context, ArrayList<AdminOrder> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdminOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.admin_order_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderAdapter.MyViewHolder holder, int position) {
        AdminOrder adminOrder=list.get(position);
        holder.orderId.setText(String.valueOf(adminOrder.getOrderId()));
        holder.customerName.setText(adminOrder.getCustomerName());
        holder.finalAmount.setText(String.valueOf(adminOrder.getFinalAmount()));

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String orderStatus = adminOrder.getOrderStatus();
                if ("To Confirm".equals(orderStatus)) {
                    intent = new Intent(context, AdminConfirmOrderActivity.class);
                } else if ("Preparing".equals(orderStatus)) {
                    intent = new Intent(context, AdminPrepareOrderActivity.class);
                }
                else if ("On Delivery".equals(orderStatus)) {
                    intent = new Intent(context, AdminOnDeliveryActivity.class);
                }
                else if ("Completed".equals(orderStatus)) {
                    intent = new Intent(context, AdminDeliverySuccessfullyActivity.class);
                }
                else if ("Cancelled".equals(orderStatus)) {
                    intent = new Intent(context, AdminDeliverySuccessfullyActivity.class);
                }
                else {
                    // Handle other cases if needed
                    return;
                }
                intent.putExtra("customerName", adminOrder.getCustomerName());
                intent.putExtra("address", adminOrder.getAddress());
                intent.putExtra("totalAmount", String.valueOf(adminOrder.getTotalAmount()));
                intent.putExtra("shippingFee", String.valueOf(adminOrder.getShippingFee()));
                intent.putExtra("discountedAmount", String.valueOf(adminOrder.getDiscountedAmount()));
                intent.putExtra("finalAmount",String.valueOf(adminOrder.getFinalAmount()));
                intent.putExtra("orderId",String.valueOf(adminOrder.getOrderId()));

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, customerName, finalAmount,btnView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId=itemView.findViewById(R.id.id_order);
            customerName=itemView.findViewById(R.id.name_user_order);
            finalAmount=itemView.findViewById(R.id.price);
            btnView= itemView.findViewById(R.id.btnView);

        }
    }
}
