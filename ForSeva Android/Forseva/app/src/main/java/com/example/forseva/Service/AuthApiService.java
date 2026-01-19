package com.example.forseva.Service;

import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.LoginRequest;
import com.example.forseva.DTO.LoginResponse;
import com.example.forseva.DTO.RegistrationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("/api/auth/login")
    Call<ApiResponse<Void>> login(@Body LoginRequest loginRequest);
    @POST("/auth/login")
    Call<ApiResponse<String>> logind(@Body LoginRequest loginRequest);

    @POST("/auth/signup")
    Call<Void> register(@Body RegistrationRequest registrationRequest);
}
