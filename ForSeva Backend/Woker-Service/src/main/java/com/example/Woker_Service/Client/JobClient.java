package com.example.Woker_Service.Client;

import com.example.Woker_Service.Dto.ApiResponse;
import com.example.Woker_Service.Dto.ReviewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// "job-service" must match the spring.application.name of your Job-Service
@FeignClient(name = "JOB-SERVICE")
public interface JobClient {

    /**
     * This method calls the Job-Service endpoint we created earlier
     * to calculate the average rating for a specific worker.
     */
    @GetMapping("/api/jobs/worker/{workerId}/average-rating")
    ApiResponse<Double> getAverageRating(@PathVariable("workerId") String workerId);

    // 2. NEW: Get Completed Jobs Count
    @GetMapping("/api/jobs/count")
    Long getWorkerJobCount(@RequestParam("workerId") String workerId);

    // 3. Optional: Get Reviews List
    @GetMapping("/api/jobs/reviews")
    List<ReviewModel> getWorkerReviews(@RequestParam("workerId") String workerId);
}
