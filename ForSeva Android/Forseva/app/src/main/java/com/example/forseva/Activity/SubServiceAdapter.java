package com.example.forseva.Activity;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.R;

import java.util.List;

public class SubServiceAdapter extends RecyclerView.Adapter<SubServiceAdapter.SubViewHolder> {

    private List<String> subServiceList;
    private OnSubServiceClickListener listener;

    public interface OnSubServiceClickListener {
        void onSubServiceClick(String subService);
    }

    public SubServiceAdapter(List<String> list, OnSubServiceClickListener listener) {
        this.subServiceList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_service, parent, false);
        return new SubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {
        String subServiceName = subServiceList.get(position);
        holder.tvTitle.setText(subServiceName);

        holder.itemView.setOnClickListener(v -> {
            // Just tell the listener (Activity) which string was clicked
            if (listener != null) {
                listener.onSubServiceClick(subServiceName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subServiceList.size();
    }

    public static class SubViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSubServiceName);
        }
    }
}
