package com.example.UserService.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    private String userId; // Linking ID from Auth-Service

    private String name; //
    private String email; //
    private String phone; //
    private String userType = "customer";
   // private String profilePicture;

    @Embedded // Implements the "address: map" requirement
    private Address address;

    //@Column(columnDefinition = "geometry(Point, 4326)")
    //Point location;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
