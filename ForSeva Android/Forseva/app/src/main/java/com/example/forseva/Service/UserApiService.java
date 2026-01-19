package com.example.forseva.Service;

import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserApiService {

    // No need to pass token here, the Interceptor handles it
    @GET("api/users/profile")
    Call<ApiResponse<UserProfile>> getProfile();

    @PUT("api/users/update")
    Call<ApiResponse<UserProfile>> updateProfile(@Body UserProfile userProfile);
}
