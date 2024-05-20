package com.thaikimhuynh.miladyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;

import java.util.List;

public class StarRatingAdapter extends RecyclerView.Adapter<StarRatingAdapter.StarViewHolder> {

    private List<String> mStarList;
    private int mSelectedPosition = RecyclerView.NO_POSITION; // Initially, no position is selected
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
        holder.starButton.setText(starRating);

        // Set colors based on selection state
        if (position == mSelectedPosition) {
            holder.starButton.setBackgroundResource(R.drawable.size_selected);
        } else {
            holder.starButton.setBackgroundResource(R.drawable.size_unselected);
        }

        holder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedPosition = mSelectedPosition;
                mSelectedPosition = position;
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(mSelectedPosition);
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
        Button starButton;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            starButton = itemView.findViewById(R.id.starTextView);
        }
    }
}
