package com.thaikimhuynh.miladyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Order;
import com.thaikimhuynh.miladyapp.model.Product;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
        void onProductClick(Product product);
    }

    public OrderAdapter(List<Order> orderList, OnItemClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirming_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtOrderNumber.setText(String.valueOf(order.getOrderId()));
        holder.txtOrderDate.setText(order.getOrderDate());
        holder.txtTotalAmount.setText("$" + order.getTotalAmount());

        // Xử lý sự kiện khi người dùng nhấn vào mỗi item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(order);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderNumber;
        TextView txtOrderDate;
        TextView txtTotalAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderNumber = itemView.findViewById(R.id.txtOrderNumber);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtTotalAmount = itemView.findViewById(R.id.txtTotalAmount);
        }
    }
}
