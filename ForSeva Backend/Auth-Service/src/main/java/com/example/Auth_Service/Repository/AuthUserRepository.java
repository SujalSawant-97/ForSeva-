package com.example.Auth_Service.Repository;

import com.example.Auth_Service.Model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    // Used during login to find the user by their unique email
    Optional<AuthUser> findByEmail(String email);
    boolean existsByEmail(String email);

    // Can be used if you allow login via phone number
    //Optional<AuthUser> findByPhone(String phone);
}
