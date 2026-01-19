package com.example.Job_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetail {
    private String id;
    private String workerName;
    private String categoryName;      // Job Type (e.g., Electrician)
    private String serviceDescription; // Description (e.g., AC Repair)
    private String scheduledAt;
    private double cost;
    private String paymentMethod;
    private String paymentStatus;
    private String status;            // PENDING, COMPLETED, etc.
    private float rating;             // Existing rating
    private String review;
}
