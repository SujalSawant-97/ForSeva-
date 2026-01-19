package com.example.Woker_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewModel {
    private String reviewerName;
    private String comment;
    private float rating; // Use float for RatingBar
    private String date;
}
