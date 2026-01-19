package com.example.Woker_Service.Model;

import lombok.Data;

import java.util.List;

@Data
public class WorkerDetails {
    private List<String> skills;
    private double hourlyRate;
    private boolean availability;
    private double rating;
    private int experienceYears;
    private String bio;
    private List<Review> reviews; // Array of maps
}
