package com.example.Job_Service.Model;

public enum JobStatus {
    PENDING,    // Initial state when customer books
    ACCEPTED,   // When a worker agrees to the task
    COMPLETED,  // After the service is rendered
    CANCELED    // If either party terminates the request
}
