package com.example.forseva.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.BookingDetailModel;
import com.example.forseva.DTO.RatingRequest;
import com.example.forseva.R;
import com.example.forseva.Service.JobApiService;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView tvWorkerName, tvJobType, tvDescription, tvScheduledAt, tvCost, tvPayment;
    private LinearLayout llRateService, llExistingReview;
    private RatingBar rbInputRating, rbExistingRating;
    private TextInputEditText etReviewInput;
    private Button btnSubmitRating;
    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        // 1. Initialize Views
        initViews();

        // 2. Get Booking ID from Intent
         bookingId = getIntent().getStringExtra("BOOKING_ID");

        // CHANGE: Check for null
        if (bookingId != null) {
            fetchBookingDetails(); // Ensure your fetch function accepts String
        } else {
            Toast.makeText(this, "Error: Booking ID missing", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no ID found
        }

        // 3. Handle Rating Submission
        btnSubmitRating.setOnClickListener(v -> submitRating());
    }

    private void initViews() {
        tvWorkerName = findViewById(R.id.tvDetailWorkerName);
        tvJobType = findViewById(R.id.tvDetailJobType);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvScheduledAt = findViewById(R.id.tvDetailScheduledAt);
        tvCost = findViewById(R.id.tvDetailCost);
        tvPayment = findViewById(R.id.tvDetailPaymentStatus);

        llRateService = findViewById(R.id.llRateService); // Input section
        llExistingReview = findViewById(R.id.llRatingSection); // Display section

        rbInputRating = findViewById(R.id.rbInputRating);
        rbExistingRating = findViewById(R.id.rbDetailRating);
        etReviewInput = findViewById(R.id.etReviewInput);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);
    }

    private void fetchBookingDetails() {
        JobApiService api = RetrofitClient.getRetrofitInstance(this).create(JobApiService.class);

        api.getJobDetails(bookingId).enqueue(new Callback<ApiResponse<BookingDetailModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<BookingDetailModel>> call, Response<ApiResponse<BookingDetailModel>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // First check your backend's success flag
                    if (response.body().isSuccess()) {
                        // REACH THE ACTUAL DATA HERE
                        BookingDetailModel data = response.body().getData();
                        displayData(data);
                    } else {
                        Toast.makeText(BookingDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            public void onFailure(Call<ApiResponse<BookingDetailModel>> call, Throwable t) {
                Toast.makeText(BookingDetailActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }




private void displayData(BookingDetailModel data) {
    // 1. Safety check: If data is null, don't proceed
    if (data == null) {
        Toast.makeText(this, "No details available", Toast.LENGTH_SHORT).show();
        return;
    }

    // 2. Populate the basic TextVews
    // Use String.valueOf() for numbers like Cost to prevent ResourceNotFound errors
    tvWorkerName.setText(data.getWorkerName());
    tvJobType.setText("Category: " + data.getCategoryName());
    tvDescription.setText("Service: " + data.getServiceDescription());
    tvScheduledAt.setText("Scheduled: " + data.getScheduledAt());
    tvCost.setText("Total Cost: ₹" + String.valueOf(data.getCost()));
    tvPayment.setText("Payment: " + data.getPaymentMethod() + " (" + data.getPaymentStatus() + ")");

    // 3. Logic for Rating Visibility based on Status
    // Ensure you use .equalsIgnoreCase to avoid string mismatch issues
    if ("COMPLETED".equalsIgnoreCase(data.getStatus())) {
        if (data.getRating() > 0) {
            // Case: Already Rated
            llRateService.setVisibility(View.GONE);
            llExistingReview.setVisibility(View.VISIBLE);

            rbExistingRating.setRating(data.getRating());

            // Check if there is a review text to display
            if (data.getReview() != null && !data.getReview().isEmpty()) {
                TextView tvReviewText = findViewById(R.id.tvDetailReview);
                tvReviewText.setText(data.getReview());
            }
        } else {
            // Case: Completed but needs rating
            llRateService.setVisibility(View.VISIBLE);
            llExistingReview.setVisibility(View.GONE);
        }
    } else {
        // Case: Pending, Confirmed, or Cancelled (Hide all rating views)
        llRateService.setVisibility(View.GONE);
        llExistingReview.setVisibility(View.GONE);
    }
}

private void submitRating() {
    float ratingValue = rbInputRating.getRating();
    String reviewText = etReviewInput.getText().toString();

    RatingRequest request = new RatingRequest(bookingId, ratingValue, reviewText);

    JobApiService api = RetrofitClient.getRetrofitInstance(this).create(JobApiService.class);

    // Call<ApiResponse<Void>> ensures we match the JSON structure
    api.submitRating(request).enqueue(new Callback<ApiResponse<Void>>() {
        @Override
        public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
            if (response.isSuccessful() && response.body() != null) {
                if (response.body().isSuccess()) {
                    Toast.makeText(BookingDetailActivity.this, "Rating Submitted!", Toast.LENGTH_SHORT).show();
                    llRateService.setVisibility(View.GONE);
                    fetchBookingDetails(); // Refresh
                }
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
            Toast.makeText(BookingDetailActivity.this, "Submission Failed", Toast.LENGTH_SHORT).show();
        }
    });
}
}
