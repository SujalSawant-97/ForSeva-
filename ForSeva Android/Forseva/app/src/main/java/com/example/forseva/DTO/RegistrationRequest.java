package com.example.forseva.DTO;

public class RegistrationRequest {
    private String email;
    private String password;
    private String role; // "CUSTOMER" or "WORKER"

    public RegistrationRequest(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
