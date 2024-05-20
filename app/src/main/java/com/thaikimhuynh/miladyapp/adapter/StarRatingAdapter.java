package com.thaikimhuynh.miladyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;

import java.util.List;

public class StarRatingAdapter extends RecyclerView.Adapter<StarRatingAdapter.StarViewHolder> {

    private List<String> mStarList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public StarRatingAdapter(List<String> starList) {
        mStarList = starList;
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_star_item_layout, parent, false);
        return new StarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        String starRating = mStarList.get(position);
        holder.starTextView.setText(starRating);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStarList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class StarViewHolder extends RecyclerView.ViewHolder {
        TextView starTextView;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            starTextView = itemView.findViewById(R.id.starTextView);
        }
    }
}
