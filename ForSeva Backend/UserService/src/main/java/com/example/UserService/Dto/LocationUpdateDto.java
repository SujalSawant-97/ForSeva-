package com.example.UserService.Dto;

import lombok.Data;

@Data
public class LocationUpdateDto {
    private String city;
    private String state;
    private String zipcode;
    private double latitude;
    private double longitude;
}
