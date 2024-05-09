package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.databinding.ViewHolderSizeBinding;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.Viewholder> {
    ArrayList<String> items;
    Context context;

    int selectedPosition = -1;
    int lastSelectedPosition = -1;

    public SizeAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SizeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        ViewHolderSizeBinding binding = ViewHolderSizeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.Viewholder holder, int position) {
        holder.binding.txtSize.setText(items.get(position));

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition=selectedPosition;
                selectedPosition=holder.getAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });


        if(selectedPosition==holder.getAdapterPosition()){
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_selected);
            holder.binding.txtSize.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.binding.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
            holder.binding.txtSize.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewHolderSizeBinding binding;
        public Viewholder(ViewHolderSizeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
