package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.PaymentGroup;
import com.thaikimhuynh.miladyapp.model.PaymentItem;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Context mContext;
    private List<PaymentGroup> mList;
    private List<PaymentItem> item_list;
    public ItemAdapter(Context mContext, List<PaymentGroup> mList){
        this.mContext = mContext;
        this.mList = mList;


    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentmethod_item, parent, false);
        return new ItemViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        PaymentGroup groupmodel = mList.get(position);
        holder.pm_name.setText(groupmodel.getItemText());

        boolean isExpandable = groupmodel.isExpandable();
        holder.nestedLayout.setVisibility(isExpandable ? View.VISIBLE: View.GONE);

        if (isExpandable){
            holder.arrow_btn.setImageResource(R.mipmap.ic_arrow_up);
        }
        else{
            holder.arrow_btn.setImageResource(R.mipmap.ic_arrow_down);
        }

        NestedAdapter adapter = new NestedAdapter(item_list);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
  
        holder.arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupmodel.setExpandable(!groupmodel.isExpandable());
                item_list = groupmodel.getItemList();
                notifyItemChanged(holder.getAdapterPosition());


            }
        });



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView pm_name;
        private ImageView arrow_btn;

        private LinearLayout linearLayout;
        private RelativeLayout nestedLayout;
        private RecyclerView nestedRecyclerView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            pm_name = itemView.findViewById(R.id.tv_paymentmethod);
            arrow_btn = itemView.findViewById(R.id.dropdown_btn);
            nestedRecyclerView = itemView.findViewById(R.id.recyclerview_mywallet);
            nestedLayout = itemView.findViewById(R.id.nested_layout);
            linearLayout = itemView.findViewById(R.id.linear_layout);



        }
    }
}
