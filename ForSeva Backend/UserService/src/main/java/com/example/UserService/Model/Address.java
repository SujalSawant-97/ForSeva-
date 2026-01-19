package com.example.UserService.Model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String city;
    private String state;
    private String zipCode;
    private double latitude; //
    private double longitude; //
}
