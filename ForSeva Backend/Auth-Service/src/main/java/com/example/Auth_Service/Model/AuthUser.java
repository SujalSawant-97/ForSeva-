package com.example.Auth_Service.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // [cite: 27]

    @Column(unique = true, nullable = false)
    private String email; // [cite: 28]

    @Column(nullable = false)
    private String password; // [cite: 29]

    @Enumerated(EnumType.STRING)
    private Role role; // [cite: 30]
}




