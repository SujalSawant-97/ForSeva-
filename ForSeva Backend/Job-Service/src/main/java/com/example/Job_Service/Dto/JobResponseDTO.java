package com.example.Job_Service.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobResponseDTO {
    private String jobId;
    private String status;
    private String serviceType;
    private LocalDateTime scheduledAt;
    private Double amount;
    private String paymentStatus;
}

