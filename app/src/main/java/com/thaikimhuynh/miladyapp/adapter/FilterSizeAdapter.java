package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thaikimhuynh.miladyapp.R;
import java.util.ArrayList;

public class FilterSizeAdapter extends RecyclerView.Adapter<FilterSizeAdapter.ViewHolder> {
    private ArrayList<String> items;
    private Context context;
    private int selectedPosition = -1;
    private OnItemClickListener mListener;

    public FilterSizeAdapter(ArrayList<String> items) {
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemClick(String size);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_holder_size, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtSize.setText(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
                if (mListener != null) {
                    mListener.onItemClick(items.get(selectedPosition));
                }
            }
        });

        if (selectedPosition == holder.getAdapterPosition()) {
            holder.sizeLayout.setBackgroundResource(R.drawable.filter_size_selected);
            holder.txtSize.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.sizeLayout.setBackgroundResource(R.drawable.filter_size_unselected);
            holder.txtSize.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSize;
        View sizeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSize = itemView.findViewById(R.id.txtSize);
            sizeLayout = itemView.findViewById(R.id.sizeLayout);
        }
    }
}
