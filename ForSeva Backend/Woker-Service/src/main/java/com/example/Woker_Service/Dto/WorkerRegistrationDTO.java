package com.example.Woker_Service.Dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkerRegistrationDTO {
    private String name;
    private String email;
    private String phone;
    private String profilePicture;
    private AddressDTO address;
    private WorkerDetailsDTO workerDetails;
}
