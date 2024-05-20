package com.thaikimhuynh.miladyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Order;

import org.w3c.dom.Text;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    String fragmentType;
    OrderAdapterListener listener;

    public OrderAdapter(List<Order> orderList, String fragmentType) {
        this.orderList = orderList;
        this.fragmentType = fragmentType;
    }
    public void setOnItemClickListener(OrderAdapterListener listener) {
        this.listener = listener;
    }
    public interface OrderAdapterListener {
        void onItemClicked(Order order);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_confirming, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtOrderNumber.setText(String.valueOf(order.getOrderId()));
        holder.txtOrderDate.setText(order.getOrderDate());
        holder.txtTotalAmount.setText(String.valueOf(order.getTotalAmount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClicked(orderList.get(position));
                    }
                }
            }
        });
        switch (fragmentType) {
            case "Confirmed":
                holder.btnAction.setText("Confirmed");
                break;
            case "Delivered":
                holder.btnAction.setText("Delivered");
                break;
            case "Received":
                holder.btnAction.setText("Received");
                break;
            default:
                holder.btnAction.setText("Completed");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderNumber, txtOrderDate, txtTotalAmount;
        Button btnAction;


        public OrderViewHolder(View itemView) {
            super(itemView);
            txtOrderNumber = itemView.findViewById(R.id.txtOrderNumber);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtTotalAmount = itemView.findViewById(R.id.txtTotalAmount);
            btnAction = itemView.findViewById(R.id.btnAction);

        }
    }
}
