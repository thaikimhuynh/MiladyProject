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
import com.thaikimhuynh.miladyapp.model.AdminHeels;


import java.util.List;


public class AdminHeelsAdapter extends BaseAdapter {
    private Context context;
    private List<AdminHeels> heelsList;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(AdminHeels heels);
    }
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(AdminHeels heels);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }


    public AdminHeelsAdapter(Context context, List<AdminHeels> heelsList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.heelsList = heelsList;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public int getCount() {
        return heelsList.size();
    }


    @Override
    public Object getItem(int position) {
        return heelsList.get(position);
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


        AdminHeels heels = heelsList.get(position);
        holder.idTextView.setText(heels.getId());
        holder.titleTextView.setText(heels.getName());
        holder.priceTextView.setText(String.valueOf(heels.getPrice()));
        holder.stockTextView.setText(String.valueOf(heels.getStock()));


        if (heels.getPicUrl() != null && !heels.getPicUrl().isEmpty()) {
            Glide.with(context).load(heels.getPicUrl()).into(holder.imageView);
        }


        convertView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(heels);
            }
        });


        holder.deleteImageView.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(heels);
            }
        });

        convertView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(heels);
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
