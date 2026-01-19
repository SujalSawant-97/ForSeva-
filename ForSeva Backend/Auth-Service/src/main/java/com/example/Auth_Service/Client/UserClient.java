package com.example.Auth_Service.Client;

import com.example.Auth_Service.Dto.UserDto;
import com.example.Auth_Service.Model.AuthUser;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// "user-service" must match the spring.application.name in User Service's properties
@FeignClient(name = "USER-SERVICE", path = "/api/users")
public interface UserClient {

    @PostMapping("/register")
    void registerUserProfile(@RequestBody UserDto userDto);
}
