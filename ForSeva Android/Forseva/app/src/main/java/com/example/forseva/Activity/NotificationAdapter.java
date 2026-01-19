package com.example.forseva.Activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.DTO.NotificationModel;
import com.example.forseva.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifViewHolder> {

    private List<NotificationModel> list;

    public NotificationAdapter(List<NotificationModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        NotificationModel notif = list.get(position);

        holder.tvTitle.setText(notif.getTitle());
        holder.tvMessage.setText(notif.getMessage());
        holder.tvTime.setText(notif.getTimestamp());

        // Optional: Change icon color based on title or type
        if (notif.getTitle().toLowerCase().contains("confirmed")) {
            holder.ivIcon.setColorFilter(Color.parseColor("#2E7D32")); // Green
        } else if (notif.getTitle().toLowerCase().contains("cancelled")) {
            holder.ivIcon.setColorFilter(Color.parseColor("#C62828")); // Red
        } else {
            holder.ivIcon.setColorFilter(Color.parseColor("#1976D2")); // Default Blue
        }
        holder.itemView.setOnClickListener(v -> {
            // Call API: /api/notifications/mark-read/{id}
            // Then change the background color of the item to white
            holder.itemView.setBackgroundColor(Color.WHITE);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotifViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvTime;
        ImageView ivIcon;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotifTitle);
            tvMessage = itemView.findViewById(R.id.tvNotifMessage);
            tvTime = itemView.findViewById(R.id.tvNotifTime);
            ivIcon = itemView.findViewById(R.id.ivNotifIcon);
        }
    }
}
