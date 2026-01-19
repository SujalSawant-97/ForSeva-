package com.example.Notification_Service.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String recipientId; // Can be userId or workerId
    private String title;
    private String message;
    private String jobId;

    private boolean read = false; // To show the "red dot" on the bell icon

    @CreationTimestamp
    private LocalDateTime createdAt;
}
