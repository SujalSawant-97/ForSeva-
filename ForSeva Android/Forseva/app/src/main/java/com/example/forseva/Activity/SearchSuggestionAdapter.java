package com.example.forseva.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.Model.ServiceCategory;
import com.example.forseva.R;

import java.util.List;

public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.ViewHolder> {

    private List<ServiceCategory> suggestionList;
    private Context context;

    public SearchSuggestionAdapter(Context context, List<ServiceCategory> suggestionList) {
        this.context = context;
        this.suggestionList = suggestionList;
    }

    public void updateList(List<ServiceCategory> newList) {
        this.suggestionList = newList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Use a simple layout: just text and a small icon
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServiceCategory model = suggestionList.get(position);
        holder.tvName.setText(model.getName());

        // Click Logic: Go to SubServiceActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubServiceActivity.class);
            intent.putExtra("SERVICE_NAME", model.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            // Ensure you create this layout: item_search_suggestion.xml
            tvName = itemView.findViewById(R.id.tvSuggestionName);
        }
    }
}
