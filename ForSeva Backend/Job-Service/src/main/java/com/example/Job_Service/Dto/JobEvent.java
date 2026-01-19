package com.example.Job_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobEvent {
    private String jobId;
    private String customerId;
    private String workerId;
    private String serviceType;
    private String status; // PENDING, ACCEPTED, etc.
    private String message;
}
