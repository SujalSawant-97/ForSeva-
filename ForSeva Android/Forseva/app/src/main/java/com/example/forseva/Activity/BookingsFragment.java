package com.example.forseva.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forseva.Client.RetrofitClient;
import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.BookingModel;
import com.example.forseva.R;
import com.example.forseva.Service.JobApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingsFragment extends Fragment {

    private RecyclerView rvBookings;
    private BookingAdapter adapter;
    private ProgressBar progressBar;
    private List<BookingModel> bookingList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        // Initialize Views
        rvBookings = view.findViewById(R.id.rvBookings);
        progressBar = view.findViewById(R.id.pbBookings);

        // Setup RecyclerView
        rvBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(bookingList, getContext());
        rvBookings.setAdapter(adapter);

        // Fetch data from Job-Service
        fetchUserBookings();

        return view;
    }

    private void fetchUserBookings() {
        progressBar.setVisibility(View.VISIBLE);

        // 1. Get Service from the authenticated Retrofit instance
        JobApiService apiService = RetrofitClient.getRetrofitInstance(getContext())
                .create(JobApiService.class);

        // 2. The Call must match the ApiResponse wrapper
        apiService.getMyBookings().enqueue(new Callback<ApiResponse<List<BookingModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookingModel>>> call, Response<ApiResponse<List<BookingModel>>> response) {
                progressBar.setVisibility(View.GONE);

                // Level 1: Check HTTP Success (200 OK)
                if (response.isSuccessful() && response.body() != null) {

                    // Level 2: Check your custom Backend Success Flag
                    if (response.body().isSuccess()) {

                        // Level 3: Unwrap the actual List from the 'data' field
                        List<BookingModel> bookings = response.body().getData();

                        if (bookings != null) {
                            bookingList.clear();
                            bookingList.addAll(bookings);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        // Show backend error message
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookingModel>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("API_ERROR", "Failed to fetch bookings", t);
                Toast.makeText(getContext(), "Network error: Check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

