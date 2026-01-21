package com.example.forseva.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.BookingRequest;
import com.example.forseva.DTO.ReviewModel;
import com.example.forseva.DTO.WorkerDetailModel;
import com.example.forseva.Model.WorkerModel;
import com.example.forseva.R;
import com.example.forseva.Service.JobApiService;
import com.example.forseva.Service.WorkerApiService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerDetailActivity extends AppCompatActivity {

    private TextView tvName, tvRating, tvDistance, tvPrice, tvBio, tvExperience, tvJobsCompleted;
    private LinearLayout llReviewsContainer;
    private Button btnBookNow;
    private String workerId;
    private String workerName;
    private String subService;
    private  double hourlyRate;
    private int distance;
    private String maincat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_detail);

        // 1. Initialize UI Elements
        initViews();

        // 2. Get Worker ID from Intent
        workerId = getIntent().getStringExtra("WORKER_ID");
        workerName = getIntent().getStringExtra("WORKER_NAME");
         maincat=getIntent().getStringExtra("MAINCATEGORY");
        // NEW: Receive these from the previous list screen
        subService = getIntent().getStringExtra("SUB_SERVICE"); // e.g., "Tap Repair"
        hourlyRate = getIntent().getDoubleExtra("HOURLY_RATE", 0.0);
        distance=getIntent().getIntExtra("DISTANCE",0);

        // 3. Fetch Data

        if (workerId!=null) {
            fetchWorkerDetails();
        }

        // 4. Handle Booking
        btnBookNow.setOnClickListener(v -> showBookingDialog());
    }

    private void initViews() {
        tvName = findViewById(R.id.tvDetailName);
        tvRating = findViewById(R.id.tvDetailRating);
        tvDistance = findViewById(R.id.tvDetailDistance);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvBio = findViewById(R.id.tvDetailBio);
        tvExperience = findViewById(R.id.tvDetailExperience);
        tvJobsCompleted = findViewById(R.id.tvDetailJobsCompleted);
        llReviewsContainer = findViewById(R.id.llReviewsContainer);
        btnBookNow = findViewById(R.id.btnBookNow);
    }

    private void fetchWorkerDetails() {
        WorkerApiService api = RetrofitClient.getRetrofitInstance(this).create(WorkerApiService.class);

        api.getWorkerDetails(workerId).enqueue(new Callback<ApiResponse<WorkerDetailModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<WorkerDetailModel>> call, Response<ApiResponse<WorkerDetailModel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    updateUI(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<WorkerDetailModel>> call, Throwable t) {
                Toast.makeText(WorkerDetailActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(WorkerDetailModel worker) {
        tvName.setText(worker.getName());
        tvRating.setText("★ " + worker.getRating());
        tvPrice.setText("₹" + worker.getHourlyRate() + "/hr");
        tvDistance.setText(worker.getDistance());
        tvBio.setText(worker.getBio());
        tvExperience.setText(distance + " Years");
        tvJobsCompleted.setText(worker.getJobsDone() + "+");

        // 2. Load Cloudinary Image (NEW)
        ImageView ivProfile = findViewById(R.id.ivDetailProfile); // Make sure this matches XML ID

        // Check if URL exists before trying to load
        if (worker.getProfileImageUrl() != null && !worker.getProfileImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(worker.getProfileImageUrl())
                    .placeholder(R.drawable.ic_person) // Show this while loading
                    .error(R.drawable.ic_person)       // Show this if URL fails
                    .circleCrop()                      // Optional: Make it round
                    .into(ivProfile);
        }else {
            // IMPORTANT: Explicitly set the default image if no URL exists
            // This prevents the "wrong image" from appearing due to recycling
            Glide.with(this).clear(ivProfile); // Optional: Clear memory
            ivProfile.setImageResource(R.drawable.ic_person);
        }

        // 3. Dynamic Reviews (Your existing logic)
        llReviewsContainer.removeAllViews();
        if (worker.getReviews() != null) {
            int limit = Math.min(worker.getReviews().size(), 3);
            for (int i = 0; i < limit; i++) {
                addReviewToLayout(worker.getReviews().get(i));
            }
        }
    }

    private void addReviewToLayout(ReviewModel review) {
        View view = getLayoutInflater().inflate(R.layout.item_review, llReviewsContainer, false);

        TextView tvUserName = view.findViewById(R.id.tvReviewerName);
        TextView tvComment = view.findViewById(R.id.tvReviewComment);
        TextView tvRating = view.findViewById(R.id.tvReviewRating);

        tvUserName.setText(review.getReviewerName());
        tvComment.setText(review.getComment());
        tvRating.setText("★ " + review.getRating());

        llReviewsContainer.addView(view);
    }

    private void showBookingDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_book_service, null);
        dialog.setContentView(view);

        TextInputEditText etDate = view.findViewById(R.id.etDate);
        TextInputEditText etTime = view.findViewById(R.id.etTime);
        Button btnConfirm = view.findViewById(R.id.btnConfirmBooking);

        // Use Calendar to hold the selected data
        final Calendar calendar = Calendar.getInstance();

        // 1. DATE PICKER LOGIC
        etDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Update UI
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                etDate.setText(dateFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 2. TIME PICKER LOGIC
        etTime.setOnClickListener(v -> {
            new TimePickerDialog(this, (view12, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Update UI
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                etTime.setText(timeFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        });

        // 3. CONFIRM BUTTON LOGIC
        btnConfirm.setOnClickListener(v -> {
            if (etDate.getText().toString().isEmpty() || etTime.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            // FORMAT TO ISO-8601 FOR SPRING BOOT (LocalDateTime)
            // Format: "2026-01-20T14:30:00"
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            String scheduledAt = isoFormat.format(calendar.getTime());

            // Call API
            performBooking(scheduledAt, dialog);
        });

        dialog.show();
    }

    private void performBooking(String scheduledAt, BottomSheetDialog dialog) {
        // Retrieve current User ID (from SharedPrefs or Session)
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String custname=   prefs.getString("USER_NAME", "");
        String city = prefs.getString("USER_CITY", "");
        String state= prefs.getString("USER_STATE", "");
        String zip= prefs.getString("USER_ZIP", "");
        String latStr = prefs.getString("USER_LAT", "0.0");
        String lonStr = prefs.getString("USER_LNG", "0.0");

        // 2. Convert String to Double
        double lat = Double.parseDouble(latStr);
        double lon = Double.parseDouble(lonStr);
        // Create Request Object
        BookingRequest request = new BookingRequest(
                workerId,
                workerName,
                custname,
                maincat,
                subService,
                scheduledAt,
                hourlyRate,
                city,
                state,
                zip,
                lat,
                lon
        );

        JobApiService api = RetrofitClient.getRetrofitInstance(this).create(JobApiService.class);
        api.createBooking(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(WorkerDetailActivity.this, "Booking Confirmed!", Toast.LENGTH_LONG).show();
                    navigateToHome();
                } else {
                    Toast.makeText(WorkerDetailActivity.this, "Booking Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(WorkerDetailActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(WorkerDetailActivity.this, HomeActivity.class); // Or HomeActivity
        // This flag clears the back stack so user can't go back to the booking screen
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close current activity
    }
}
