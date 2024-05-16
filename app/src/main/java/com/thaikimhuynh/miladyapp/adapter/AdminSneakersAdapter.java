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
import com.thaikimhuynh.miladyapp.model.AdminSneakers;

import java.util.List;

public class AdminSneakersAdapter extends BaseAdapter {
    private Context context;
    private List<AdminSneakers> sneakersList;
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(AdminSneakers sneakers);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(AdminSneakers sneakers);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public AdminSneakersAdapter(Context context, List<AdminSneakers> sneakersList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.sneakersList = sneakersList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return sneakersList.size();
    }

    @Override
    public Object getItem(int position) {
        return sneakersList.get(position);
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

        AdminSneakers sneakers = sneakersList.get(position);
        holder.idTextView.setText(sneakers.getId());
        holder.titleTextView.setText(sneakers.getName());
        holder.priceTextView.setText(String.valueOf(sneakers.getPrice()));
        holder.stockTextView.setText(String.valueOf(sneakers.getStock()));

        if (sneakers.getPicUrl() != null && !sneakers.getPicUrl().isEmpty()) {
            Glide.with(context).load(sneakers.getPicUrl()).into(holder.imageView);
        }

        convertView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(sneakers);
            }
        });

        holder.deleteImageView.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(sneakers);
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
