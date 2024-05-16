package com.thaikimhuynh.miladyapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.thaikimhuynh.miladyapp.payment.AddNewWalletActivity;
import com.thaikimhuynh.miladyapp.payment.AddNewWalletCartActivity;
import com.thaikimhuynh.miladyapp.payment.WalletInformationActivity;

import java.util.List;
public class ItemWithButtonAdapter extends RecyclerView.Adapter<ItemWithButtonAdapter.ItemWithButtonViewHolder>  {
    private List<PaymentGroup> mList;
    public ItemWithButtonAdapter(Context mContext, List<PaymentGroup> mList){
        this.mList = mList;
    }

    @NonNull
    @Override
    public ItemWithButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentmethod_card_item, parent, false);
        return new ItemWithButtonViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ItemWithButtonAdapter.ItemWithButtonViewHolder holder, int position) {
        PaymentGroup groupmodel = mList.get(position);
        Log.d("groupmdodel", "groupmodel name la" + groupmodel.getItemText());
        List<PaymentItem> item_list = groupmodel.getItemList();


        NestedAdapterWithButton adapter = new NestedAdapterWithButton(item_list);
        holder.pm_name.setText(groupmodel.getItemText());

        boolean isExpandable = groupmodel.isExpandable();
        if ((groupmodel.getItemText().equalsIgnoreCase("cod")) && !groupmodel.isCodSelected()) {
            holder.arrow_btn.setImageResource(R.mipmap.select_button);
            holder.nestedLayout.setVisibility(View.GONE);

        } else {
            holder.arrow_btn.setVisibility(View.VISIBLE);
            holder.nestedLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
            holder.arrow_btn.setImageResource(isExpandable ? R.mipmap.ic_arrow_up : R.mipmap.ic_arrow_down);
        }




        adapter.setData(position);

        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
        adapter.setNestedAdapterListener(new NestedAdapterWithButton.NestedAdapterListener() {
            @Override
            public void onItemClicked(int position_v, int tag_v) {
                // Handle the clicked item in the parent adapter
                PaymentGroup group = mList.get(tag_v);

                group.setSelectedItemPosition(position_v);

                for (int i = 0; i < mList.size(); i++) {
                    if (i != tag_v) {
                        PaymentGroup currentItem = mList.get(i);
                        if (currentItem.getSelectedItemPosition() != -1)
                        {
                            // set others -1, false
                            currentItem.getItemList().get(currentItem.getSelectedItemPosition()).setSelected(false);
                            currentItem.setSelectedItemPosition(-1);

                            // Notify the adapter of the change
                            notifyItemChanged(i);

                        }
                        if (currentItem.getItemText().equalsIgnoreCase("cod"))
                        {
                            currentItem.setCodSelected(false);
                            notifyItemChanged(i);
                        }
                        }
                    }



                    }
        });

        holder.arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupmodel.getItemText().equalsIgnoreCase("cod")) {
                    holder.arrow_btn.setImageResource(R.mipmap.selected_button);
                    for (int i = 0; i < mList.size(); i++) {
                            PaymentGroup currentItem = mList.get(i);
                            if (currentItem.getSelectedItemPosition() != -1)
                            {
                                currentItem.getItemList().get(currentItem.getSelectedItemPosition()).setSelected(false);
                                currentItem.setSelectedItemPosition(-1);
                                // Notify the adapter of the change
                                notifyItemChanged(i);
                            }

                    }


                } else {
                    // For other groups, toggle expand/collapse logic
                    groupmodel.setExpandable(!groupmodel.isExpandable());
                    notifyItemChanged(holder.getAdapterPosition());
                }

            }
        });
        Context context = holder.itemView.getContext();

        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentGroup groupmodel = mList.get(position);
                String wallet_type = groupmodel.getItemText();
                String layout = wallet_type.equals("E-wallet") ? "1" : "2";
                Intent intent = new Intent(context, AddNewWalletCartActivity.class);
                intent.putExtra("layout", layout);

                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return  mList.size() ;

    }

    public class ItemWithButtonViewHolder extends RecyclerView.ViewHolder{
        private TextView pm_name;
        private ImageView arrow_btn;

        private LinearLayout linearLayout;
        private RelativeLayout nestedLayout;
        private RecyclerView nestedRecyclerView;
        private Button btn_add;


        public ItemWithButtonViewHolder(@NonNull View itemView) {
            super(itemView);

            pm_name = itemView.findViewById(R.id.tv_paymentmethod_cart);
            arrow_btn = itemView.findViewById(R.id.dropdown_btn_cart);
            nestedRecyclerView = itemView.findViewById(R.id.recyclerview_mywallet_cart);
            nestedLayout = itemView.findViewById(R.id.nested_layout_cart);
            btn_add = itemView.findViewById(R.id.btn_add_wallet);



        }
    }
}
