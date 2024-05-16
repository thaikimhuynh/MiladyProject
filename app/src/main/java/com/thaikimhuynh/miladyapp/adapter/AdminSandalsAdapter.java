package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.AdminSandals;

import java.util.List;

public class AdminSandalsAdapter extends BaseAdapter {
    private Context context;
    private List<AdminSandals> sandalsList;
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(AdminSandals sandals);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(AdminSandals sandals);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public AdminSandalsAdapter(Context context, List<AdminSandals> sandalsList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.sandalsList = sandalsList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return sandalsList.size();
    }

    @Override
    public Object getItem(int position) {
        return sandalsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_product_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView15);
            holder.idTextView = convertView.findViewById(R.id.id_order);
            holder.titleTextView = convertView.findViewById(R.id.name_product);
            holder.priceTextView = convertView.findViewById(R.id.price);
            holder.stockTextView = convertView.findViewById(R.id.stock);
            holder.deleteImageView = convertView.findViewById(R.id.ic_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AdminSandals sandals = sandalsList.get(position);
        holder.idTextView.setText(sandals.getId());
        holder.titleTextView.setText(sandals.getName());
        holder.priceTextView.setText(String.valueOf(sandals.getPrice()));
        holder.stockTextView.setText(String.valueOf(sandals.getStock()));

        if (sandals.getPicUrl() != null && !sandals.getPicUrl().isEmpty()) {
            Glide.with(context).load(sandals.getPicUrl()).into(holder.imageView);
        }

        convertView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(sandals);
            }
        });

        holder.deleteImageView.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(sandals);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        public View deleteImageView;
        ImageView imageView;
        TextView idTextView;
        TextView titleTextView;
        TextView priceTextView;
        TextView stockTextView;
    }
}
