package com.example.Job_Service.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    // The rating given by the customer (usually 1 to 5)
    private String bookingId;

    @NotNull
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private float rating;

    // The text feedback provided by the customer
    @NotBlank(message = "Comment cannot be empty")
    private String review;
}
