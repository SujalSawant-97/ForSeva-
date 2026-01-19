package com.example.Job_Service.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String jobId;

    private String customerId;// Reference to User-Service
    private String customerName;
    private String workerId;   // Reference to Worker-Service
    private String workerName;
    private String serviceType;
    private String jobDescription;

    @Enumerated(EnumType.STRING) // Saves as VARCHAR in PostgreSQL
    @Column(nullable = false)
    private JobStatus status = JobStatus.PENDING; // Default value
     // pending, accepted, completed, canceled

    private LocalDateTime scheduledAt;

    @Embedded
    private JobLocation location; // Hierarchical map logic

    @Embedded
    private PaymentDetails paymentDetails; // Hierarchical map logic

    @Embedded
    private CustomerRating customerRating; // Hierarchical map logic

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
