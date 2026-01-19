package com.example.Woker_Service.Dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkerDetailsDTO {
    private List<String> skills;
    private double hourlyRate;
}
