package com.example.forseva.Service;

import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.NotificationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationApiService {
    @GET("/api/notifications")
    Call<ApiResponse<List<NotificationModel>>> getUserNotifications();

    @GET("/api/notifications/unread-count")
    Call<ApiResponse<Integer>> getUnreadCount();
}
