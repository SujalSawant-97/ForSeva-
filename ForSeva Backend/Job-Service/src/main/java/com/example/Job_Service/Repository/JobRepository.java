package com.example.Job_Service.Repository;

import com.example.Job_Service.Model.Job;
import com.example.Job_Service.Model.JobStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    List<Job> findByCustomerId(String customerId);
    List<Job> findByWorkerId(String workerId);
    @Query("SELECT AVG(j.customerRating.rating) FROM Job j WHERE j.workerId = :workerId AND j.customerRating.rating IS NOT NULL")
    Double getAverageRatingByWorkerId(String workerId);
    long countByWorkerIdAndStatus(String workerId, JobStatus status);
    @Query("SELECT j FROM Job j WHERE j.workerId = :workerId AND j.customerRating.review IS NOT NULL ORDER BY j.scheduledAt DESC")
    List<Job> findReviewsByWorkerId(@Param("workerId") String workerId, Pageable pageable);

}
