package com.example.Job_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewModel {
    private String reviewerName;
    private String comment;
    private float rating; // Use float for RatingBar
    private String date;
}
