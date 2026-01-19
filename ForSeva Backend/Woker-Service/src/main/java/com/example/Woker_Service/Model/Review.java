package com.example.Woker_Service.Model;

import lombok.Data;

@Data
class Review {
    private String customerId;
    private double rating;
    private String reviewText;
}
