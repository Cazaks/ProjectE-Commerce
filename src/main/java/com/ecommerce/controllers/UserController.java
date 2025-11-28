package com.ecommerce.controllers;

import com.ecommerce.DTOs.request.LoginRequest;
import com.ecommerce.DTOs.request.RegistrationRequest;
import com.ecommerce.DTOs.response.LoginResponse;
import com.ecommerce.DTOs.response.RegistrationResponse;
import com.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        RegistrationResponse response = userService.registerUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.loginUser(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promote/{userId}")
    public ResponseEntity<String> promote(@PathVariable("userId") String userId) {
        userService.promoteToSeller(userId);
        return ResponseEntity.ok("User promoted to Seller successfully");
    }

}
