package com.example.Woker_Service.Dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkerDetailSearch {
    private String id;
    private String name;
    private double rating;        // tvRating
    private double hourlyRate;    // tvPrice
    private String distance;      // tvDistance (e.g., "2.5 km")
    private String bio;           // tvBio
    private int experience;       // tvExperience
    private int jobsDone;         // tvJobsCompleted
    private String profileImageUrl; // NEW: Cloudinary URL
    private List<ReviewModel> reviews;
}
