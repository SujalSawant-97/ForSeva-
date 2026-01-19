package com.example.UserService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates Getters, Setters, toString, equals, and hashCode
@NoArgsConstructor // Generates the mandatory empty constructor for JSON parsing
@AllArgsConstructor // Generates a constructor for all fields
public class UserRequestDto {
    private String fullName;
    private String email;
    private String phone;
    private LocationUpdateDto address;
}
