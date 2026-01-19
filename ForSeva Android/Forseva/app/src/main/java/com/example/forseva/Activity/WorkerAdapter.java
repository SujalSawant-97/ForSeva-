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

import com.bumptech.glide.Glide;
import com.example.forseva.Model.WorkerModel;
import com.example.forseva.R;

import java.util.List;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder> {

    private List<WorkerModel> workerList;
    private Context context;
    private String subservice;
    private String maincat;

    public WorkerAdapter(List<WorkerModel> workerList,String subservice, String maincat,Context context) {
        this.workerList = workerList;
        this.context = context;
        this.subservice=subservice;
        this.maincat=maincat;
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_worker, parent, false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        WorkerModel worker = workerList.get(position);

        holder.tvName.setText(worker.getName());
        holder.tvRating.setText("★ " + worker.getRating());
        holder.tvPrice.setText("₹" + worker.getHourlyRate() + "/hr");
        holder.tvDistance.setText(worker.getDistance());
        if (worker.getProfilePic() != null && !worker.getProfilePic().isEmpty()) {
            Glide.with(context)
                    .load(worker.getProfilePic())
                    .placeholder(R.drawable.ic_person) // Show this while loading
                    .error(R.drawable.ic_person)       // Show this if URL fails
                    .circleCrop()                      // Optional: Make it round
                    .into(holder.ivProfile);
        }else {
            // IMPORTANT: Explicitly set the default image if no URL exists
            // This prevents the "wrong image" from appearing due to recycling
            Glide.with(context).clear(holder.ivProfile); // Optional: Clear memory
            holder.ivProfile.setImageResource(R.drawable.ic_person);
        }

        // Handle selection
        holder.itemView.setOnClickListener(v -> {
            // Passing selected worker data to the next screen (Booking/Details)
            Intent intent = new Intent(context, WorkerDetailActivity.class);
            intent.putExtra("WORKER_ID", worker.getId());
            intent.putExtra("WORKER_NAME", worker.getName());
            intent.putExtra("MAINCATEGORY",maincat);
            intent.putExtra("SUB_SERVICE",subservice);
            intent.putExtra("HOURLY_RATE",worker.getHourlyRate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return workerList.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRating, tvPrice, tvDistance;
        ImageView ivProfile;

        public WorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvWorkerName);
            tvRating = itemView.findViewById(R.id.tvWorkerRating);
            tvPrice = itemView.findViewById(R.id.tvWorkerPrice);
            tvDistance = itemView.findViewById(R.id.tvWorkerDistance);
            ivProfile = itemView.findViewById(R.id.ivWorkerProfile);
        }
    }
}
