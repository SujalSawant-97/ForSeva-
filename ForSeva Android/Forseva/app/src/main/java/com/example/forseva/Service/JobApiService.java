package com.example.forseva.Service;

import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.BookingDetailModel;
import com.example.forseva.DTO.BookingModel;
import com.example.forseva.DTO.BookingRequest;
import com.example.forseva.DTO.RatingRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JobApiService {
    @GET("/api/jobs/my-bookings")
    Call<ApiResponse<List<BookingModel>>> getMyBookings();

    @GET("/api/jobs/{id}")
    Call<ApiResponse<BookingDetailModel>> getJobDetails(@Path("id") String id);

    @POST("/api/jobs/rate")
    Call<ApiResponse<Void>> submitRating(@Body RatingRequest ratingRequest);

    @POST("api/jobs/book")
    Call<ApiResponse<String>> createBooking(@Body BookingRequest request);
}
