package com.example.forseva.DTO;

public class LoginResponse {
    private String token;
    private String role; // matches your backend "CUSTOMER" or "WORKER"

    public String getToken() { return token; }
    public String getRole() { return role; }
}
