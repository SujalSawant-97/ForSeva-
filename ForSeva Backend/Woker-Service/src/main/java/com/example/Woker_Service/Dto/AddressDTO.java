package com.example.Woker_Service.Dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String city;
    private String state;
    private String zipCode;
    private double latitude;
    private double longitude;
}
