package com.example.forseva.DTO;

public class ReviewModel {
    private String reviewerName;
    private String comment;
    private float rating; // Use float for RatingBar
    private String date;

    public ReviewModel(String reviewerName, String comment, float rating, String date) {
        this.reviewerName = reviewerName;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
