package com.example.forseva.Service;

import com.example.forseva.DTO.ApiResponse;
import com.example.forseva.DTO.WorkerDetailModel;
import com.example.forseva.Model.WorkerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WorkerApiService {

    @GET("api/workers/search")
    Call<ApiResponse<List<WorkerModel>>> getFilteredWorkers(
            @Query("category") String category,
            @Query("lat") double lat,
            @Query("lng") double lng
    );
    @GET("api/workers/{id}/details")
    Call<ApiResponse<WorkerDetailModel>> getWorkerDetails(@Path("id") String  workerId);
}
