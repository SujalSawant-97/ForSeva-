package com.example.forseva.DTO;

public class RatingRequest {
    private String bookingId;
    private float rating;
    private String review;

    // Constructor
    public RatingRequest(String  bookingId, float rating, String review) {
        this.bookingId = bookingId;
        this.rating = rating;
        this.review = review;
    }

    // Getters and Setters (Required for GSON serialization)
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }
}
