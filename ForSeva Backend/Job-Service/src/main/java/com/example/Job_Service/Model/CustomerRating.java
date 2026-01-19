package com.example.Job_Service.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Embeddable
@Data
public class CustomerRating {
    @Column(name = "job_rating")
    private Double rating;
    private String review;
    @CreationTimestamp
    @Column(name = "rating_created_at")
    private LocalDateTime createdAt;
}
