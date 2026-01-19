package com.example.forseva.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.DTO.BookingModel;
import com.example.forseva.R;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<BookingModel> list;
    private Context context;

    public BookingAdapter(List<BookingModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Here we link the item_booking layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingModel booking = list.get(position);

        holder.tvService.setText(booking.getServiceName());
        holder.tvDate.setText(booking.getBookingDate());
        holder.tvStatus.setText(booking.getStatus());

        // Dynamic Status Coloring
        String status = booking.getStatus().toUpperCase();
        if (status.equals("COMPLETED")) {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // Green
        } else if (status.equals("PENDING")) {
            holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")); // Orange
        } else if (status.equals("CANCELLED")) {
            holder.tvStatus.setTextColor(Color.parseColor("#C62828")); // Red
        }

        // Clicking an item opens the Detailed View
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailActivity.class);

            // 2. Pass the unique ID of the booking
            // Make sure your BookingModel has an getId() method
            intent.putExtra("BOOKING_ID", booking.getId());

            // 3. Start the activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvService, tvDate, tvStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvService = itemView.findViewById(R.id.tvBookingService);
            tvDate = itemView.findViewById(R.id.tvBookingDate);
            tvStatus = itemView.findViewById(R.id.tvStatusBadge);
        }
    }
}
