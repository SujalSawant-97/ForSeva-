package com.example.Woker_Service.Model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document(collection = "workers")
public class Worker {
    @Id
    private String userId; // ID from Auth-Service
    private String name;
    private String email;
    private String phone;
    private String userType = "worker";
    private int experienceYears;
    private String profilePicture;

    private Address address; // Reusing logic but in a Document format

    private WorkerDetails workerDetails; // The nested Map from your schema

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

