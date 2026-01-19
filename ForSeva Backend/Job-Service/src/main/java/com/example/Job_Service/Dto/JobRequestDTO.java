package com.example.Job_Service.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobRequestDTO {
    private String workerId;
    private String workerNmae;
    private String customerName;
    private String serviceName;        // e.g., "Plumbing"
    private String serviceDescription; // e.g., "Tap Repair"
    private String scheduledAt;        // ISO format: "2026-01-16T14:30:00"
    private double price;              // Hourly rate or fixed price
    private String city;
    private String state;
    private String zipcode;
    private Double lat;
    private Double longi;

}
