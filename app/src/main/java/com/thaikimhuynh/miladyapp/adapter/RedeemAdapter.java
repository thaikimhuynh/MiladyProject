package com.thaikimhuynh.miladyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thaikimhuynh.miladyapp.CouponDetailsActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.RedeemPoints;

import java.util.ArrayList;

public class RedeemAdapter extends RecyclerView.Adapter<RedeemAdapter.MyViewHolder> {
    Context context;
    ArrayList<RedeemPoints> list;

    public RedeemAdapter(Context context, ArrayList<RedeemPoints> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_coupon_recyclerview, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RedeemPoints redeemPoints = list.get(position);
        holder.title.setText(redeemPoints.getTitle());
        holder.content.setText(redeemPoints.getContent());
        holder.programname.setText(redeemPoints.getProgramname());
        holder.pointRequired.setText(redeemPoints.getPointRequired() + "P");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CouponDetailsActivity.class);
                intent.putExtra("title", redeemPoints.getTitle());
                intent.putExtra("description", redeemPoints.getDescription());
                intent.putExtra("voucherCode", redeemPoints.getVoucherCode());
                context.startActivity(intent);
            }
        });
        holder.pointRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PointWallet");
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("VoucherWallet");

                String userId = getUserId();
                Query query = reference.orderByChild("userId").equalTo(userId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            Integer totalPoints = userSnapshot.child("totalPoint").getValue(Integer.class);
                            if (totalPoints != null) {
                                Long pointRequired = Long.parseLong(redeemPoints.getPointRequired());
                                if (totalPoints >= pointRequired) {
                                    DatabaseReference voucherWalletRef = reference2.child(userId).child("voucherItems");
                                    String voucherId = voucherWalletRef.push().getKey();
                                    if (voucherId != null) {
                                        DatabaseReference voucherItemRef = voucherWalletRef.child(voucherId);
                                        voucherItemRef.setValue(redeemPoints);
                                        Long newTotalPoints = totalPoints - pointRequired;
                                        userSnapshot.child("totalPoint").getRef().setValue(newTotalPoints);
                                        Toast.makeText(context, "Voucher redeemed successfully!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } else {
                                    Toast.makeText(context, "You don't have enough points to redeem this voucher", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(context, "You don't have enough points to redeem this voucher", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        Toast.makeText(context, "You don't have enough points to redeem this voucher", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
            }

            private String getUserId() {
                SharedPreferences sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
                return sharedPreferences.getString("user_id", "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, programname, pointRequired;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewName);
            content = itemView.findViewById(R.id.textViewDescription);
            programname = itemView.findViewById(R.id.textViewProgramName);
            pointRequired = itemView.findViewById(R.id.txtPoints);
        }
    }
}
