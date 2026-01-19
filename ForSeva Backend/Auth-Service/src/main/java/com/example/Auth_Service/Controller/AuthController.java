package com.example.Auth_Service.Controller;

import com.example.Auth_Service.Dto.ApiResponse;
import com.example.Auth_Service.Dto.LoginRequest;
import com.example.Auth_Service.Model.AuthUser;
import com.example.Auth_Service.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup") // [cite: 23]
    public ResponseEntity<String> signup(@RequestBody AuthUser user) {
        return ResponseEntity.ok(authService.register(user));
    }

    //use this if passing token in header
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<Void>> login(@RequestBody LoginRequest request) {
//        String token = authService.login(request.getEmail(), request.getPassword());
//
//        return ResponseEntity.ok()
//                .header("Authorization", "Bearer " + token)
//                .body(ApiResponse.success(null)); // Success with no data body
//    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(ApiResponse.success(token)); // Success with no data body
    }
}
