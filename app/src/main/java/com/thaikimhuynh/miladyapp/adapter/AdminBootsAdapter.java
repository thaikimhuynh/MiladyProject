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
import com.thaikimhuynh.miladyapp.model.AdminBoots;

import java.util.List;

public class AdminBootsAdapter extends BaseAdapter {
    private Context context;
    private List<AdminBoots> bootsList;
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(AdminBoots boots);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(AdminBoots boots);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public AdminBootsAdapter(Context context, List<AdminBoots> bootsList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.bootsList = bootsList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return bootsList.size();
    }

    @Override
    public Object getItem(int position) {
        return bootsList.get(position);
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

        AdminBoots boots = bootsList.get(position);
        holder.idTextView.setText(boots.getId());
        holder.titleTextView.setText(boots.getName());
        holder.priceTextView.setText(String.valueOf(boots.getPrice()));
        holder.stockTextView.setText(String.valueOf(boots.getStock()));

        if (boots.getPicUrl() != null && !boots.getPicUrl().isEmpty()) {
            Glide.with(context).load(boots.getPicUrl()).into(holder.imageView);
        }

        convertView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(boots);
            }
        });

        holder.deleteImageView.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(boots);
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
