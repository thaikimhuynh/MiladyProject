package com.thaikimhuynh.miladyapp.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.thaikimhuynh.miladyapp.ProductListActivity;
import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Category;


public class CategoryAdapter extends FirebaseRecyclerAdapter<Category, CategoryAdapter.CategoryViewHolder> {

    public CategoryAdapter(
            @NonNull FirebaseRecyclerOptions<Category> options)
    {
        super(options);
    }


    @Override
    protected void
    onBindViewHolder(@NonNull CategoryViewHolder holder,
                     int position, @NonNull Category model)
    {




        holder.category_name.setText(model.getName());

        int[] categoryImageResIds  = {
                R.mipmap.ic_heels,
                R.mipmap.ic_sandals,
                R.mipmap.ic_sneakers,
                R.mipmap.ic_boots
        };
        int imageResId = categoryImageResIds[position];

        Glide.with(holder.itemView)
                .load(imageResId)
                .into(holder.category_image);

        if (position % 2 == 0) {
            holder.cardLayout.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pink_primary));
        } else {
            holder.cardLayout.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pink_second));
        }
        Context context = holder.itemView.getContext();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra("categoryId", model.getId());
                intent.putExtra("categoryName", model.getName());
                context.startActivity(intent);
            }
        });



    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView cardLayout;
        TextView category_name;
        ImageView category_image;
        public CategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);

            category_name = itemView.findViewById(R.id.tv_category_name);
            category_image = itemView.findViewById(R.id.imageView_icon);
            cardLayout = itemView.findViewById(R.id.cardView_category);
        }
    }
}
