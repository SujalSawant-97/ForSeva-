package com.example.Job_Service.Controller;


import com.example.Job_Service.Dto.*;
import com.example.Job_Service.Model.Job;
import com.example.Job_Service.Model.JobStatus;
import com.example.Job_Service.Repository.JobRepository;
import com.example.Job_Service.Service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    // 1. Create a new Job (Booking)
    @PostMapping("/book")
    public ResponseEntity<ApiResponse<String>> createJob(
            @RequestHeader("X-User-Id") String customerId,
            @RequestBody JobRequestDTO requestDTO) {
        jobService.createJob(requestDTO,customerId);
        return ResponseEntity.ok(ApiResponse.success("Booking Confirmed" ));
    }

    // 2. Update Job Status (Accepted/Completed/Canceled)
    @PatchMapping("/{jobId}/status")
    public ResponseEntity<ApiResponse<String >> updateStatus(
            @PathVariable String jobId,
            @RequestHeader("X-User-Id") String workerId,
            @RequestParam JobStatus status) {
        jobService.updateStatus(jobId, status,workerId);
        return ResponseEntity.ok(ApiResponse.success("Job status updated successfully"));
    }

    // 3. Get all jobs for the logged-in user (Customer View)
    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getCustomerJobs(
            @RequestHeader("X-User-Id") String customerId) {
        return ResponseEntity.ok(ApiResponse.success(jobService.getJobsByCustomer(customerId)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDetail>> getJobsById(
            @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(jobService.getJobById(id)));
    }

    // 4. Get all jobs assigned to a worker (Worker View)
    @GetMapping("/my-tasks")
    public ResponseEntity<ApiResponse<List<Job>>> getWorkerJobs(
            @RequestHeader("X-User-Id") String workerId) {
        return ResponseEntity.ok(ApiResponse.success(jobService.getJobsByWorker(workerId)));
    }
    @PatchMapping("/rate")
    @Valid
    public ResponseEntity<ApiResponse<String>> rateJob( @RequestBody ReviewDTO reviewDto) {
        try {
            jobService.rateAndReviewJob(reviewDto.getBookingId(), reviewDto);
            return ResponseEntity.ok(ApiResponse.success("Feedback submitted successfully!"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }
    @GetMapping("/worker/{workerId}/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAvgRating(@PathVariable String workerId) {
        return ResponseEntity.ok(ApiResponse.success(jobService.getAverageRatingForWorker(workerId)));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getJobCount(@RequestParam String workerId) {
        // Assuming you have an Enum or String for status "COMPLETED"
        long count = jobRepository.countByWorkerIdAndStatus(workerId, JobStatus.COMPLETED);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewModel>> getWorkerReviews(@RequestParam String workerId) {
        List<ReviewModel> reviews = jobService.getTop3Reviews(workerId);
        return ResponseEntity.ok(reviews);
    }
}
