package com.example.forseva.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.Model.ServiceCategory;
import com.example.forseva.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<ServiceCategory> categories;
    private Context context;

    public CategoryAdapter(List<ServiceCategory> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This line "connects" your item_category layout to the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder,int position) {
        ServiceCategory category = categories.get(holder.getAdapterPosition());
        holder.tvName.setText(category.getName());
        holder.ivIcon.setImageResource(category.getIconResId());

        // Step 1: Navigate to SubServiceActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubServiceActivity.class);

            // Pass the category details dynamically
            intent.putExtra("SERVICE_NAME", category.getName());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
