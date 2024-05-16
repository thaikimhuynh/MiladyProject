package com.thaikimhuynh.miladyapp.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.checkout.CheckOutActivity;
import com.thaikimhuynh.miladyapp.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addressList;
    private int selectedPosition = -1;

    public AddressAdapter(List<Address> addressList) {
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.txtName.setText(address.getName());
        holder.txtPhone.setText(address.getPhone());
        holder.txtAddress.setText(address.getAddress());
        Log.d("AddressAdapter", "onBindViewHolder called for position: " + position);

        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.grey));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            setSelectedPosition(position); // Cập nhật màu nền của mục được nhấn
        });

        // Trong phương thức onBindViewHolder của AddressAdapter
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy địa chỉ được nhấn
                Address address = addressList.get(position);

                // Tạo Intent để chuyển đến CheckoutActivity
                Intent intent = new Intent(v.getContext(), CheckOutActivity.class);

                // Đặt dữ liệu vào Intent
                intent.putExtra("address_name", address.getName());
                intent.putExtra("address_address", address.getAddress());
                intent.putExtra("address_phone", address.getPhone());

                // Chuyển đến CheckoutActivity
                v.getContext().startActivity(intent);
            }
        });

    }

    public void setSelectedPosition(int position) {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelectedPosition); // Cập nhật màu nền của mục trước đó
        notifyItemChanged(selectedPosition); // Cập nhật màu nền của mục mới được chọn
    }


    @Override
    public int getItemCount() {
        return addressList.size();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPhone, txtAddress;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAddress = itemView.findViewById(R.id.txtAddress);
        }
    }
}