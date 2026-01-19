package com.example.Auth_Service.Service;

import com.example.Auth_Service.Client.UserClient;
import com.example.Auth_Service.Dto.UserDto;
import com.example.Auth_Service.Model.AuthUser;
import com.example.Auth_Service.Repository.AuthUserRepository;
import com.example.Auth_Service.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthUserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserClient userClient;

    public String register(AuthUser user) {
        if (repository.existsByEmail(user.getEmail())) {
            // Stop everything immediately. Do not save. Do not call User Service.
            throw new RuntimeException("Error: Email is already in use!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // [cite: 29, 163]
        AuthUser savedUser=repository.save(user);
        UserDto userDto=new UserDto();
        userDto.setUserId(savedUser.getId().toString());
        userDto.setEmail(savedUser.getEmail());

        try {
            userClient.registerUserProfile(userDto);
        } catch (Exception e) {
            // Rollback if User Service is down
            repository.delete(savedUser);
            throw new RuntimeException("Signup failed: User Service is unavailable.");
        }
        return "User registered successfully";
    }

    public String login(String email, String password) {
        AuthUser user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(user); // [cite: 20]
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
