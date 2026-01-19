package com.example.forseva.DTO;

import java.util.List;

public class WorkerDetailModel {
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

    public WorkerDetailModel(String id, String name, double rating, int experience, String distance, String bio, double hourlyRate, int jobsDone, String profileImageUrl, List<ReviewModel> reviews) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.experience = experience;
        this.distance = distance;
        this.bio = bio;
        this.hourlyRate = hourlyRate;
        this.jobsDone = jobsDone;
        this.profileImageUrl = profileImageUrl;
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getJobsDone() {
        return jobsDone;
    }

    public void setJobsDone(int jobsDone) {
        this.jobsDone = jobsDone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewModel> reviews) {
        this.reviews = reviews;
    }
}
