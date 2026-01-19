package com.example.UserService.Controller;

import com.example.UserService.Dto.ApiResponse;
import com.example.UserService.Dto.LocationUpdateDto;
import com.example.UserService.Dto.UserDto;
import com.example.UserService.Dto.UserRequestDto;
import com.example.UserService.Model.User;
import com.example.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register") // [cite: 50]
    public ResponseEntity<String > register(
            @RequestBody UserDto dto) {
        userService.registerUser( dto);
        return ResponseEntity.ok("Registered Succesfully");
    }

    @GetMapping("/profile") // [cite: 52]
    public ResponseEntity<ApiResponse<UserRequestDto>> getUser(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUser(userId)));
    }
    @PutMapping("/update") // [cite: 50]
    public ResponseEntity<ApiResponse<User>> update(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.success(userService.upDateUser(userId, dto)));
    }

//    @PutMapping("/profile") // [cite: 52]
//    public ResponseEntity<ApiResponse<User>> getUser(@RequestHeader("X-User-Id") String userId,@RequestBody User user) {
//        return ResponseEntity.ok(ApiResponse.success(userService.getUser(userId)));
//    }


//    @PutMapping("/location")
//    public ResponseEntity<ApiResponse<String>> updateLocation(
//            @RequestHeader("X-User-Id") String userId,
//            @RequestBody LocationUpdateDto dto) {
//
//        userService.updateLocation(userId, dto);
//        return ResponseEntity.ok(ApiResponse.success("Location updated successfully"));
//    }
}
