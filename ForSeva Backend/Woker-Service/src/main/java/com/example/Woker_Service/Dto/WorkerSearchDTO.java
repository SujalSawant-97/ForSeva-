package com.example.Woker_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerSearchDTO {
    private String id;
    private String name;
    private String profilePic;
    private double rating;
    private double hourlyRate;
    private String distance; // Optional
}
