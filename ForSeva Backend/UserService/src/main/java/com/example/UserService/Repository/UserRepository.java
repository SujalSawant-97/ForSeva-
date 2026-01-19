package com.example.UserService.Repository;

import com.example.UserService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Custom finder to locate user by email if needed [cite: 46]
    Optional<User> findByEmail(String email);

    // Custom finder for phone number [cite: 45]
    Optional<User> findByPhone(String phone);
}
