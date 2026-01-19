package com.example.Job_Service.Model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class JobLocation {
    private String city;
    private String state;
    private String zipCode;
    private double latitude;
    private double longitude;
}
