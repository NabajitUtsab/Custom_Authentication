package com.example.Custom_Authentication.controller;

import com.example.Custom_Authentication.dto.LoginRequest;
import com.example.Custom_Authentication.dto.RegisterRequest;
import com.example.Custom_Authentication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest);

        return ResponseEntity.ok("User registered successfully. Check mail. ");
    }


    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        authService.verifyUser(token);

        return ResponseEntity.ok("Email verified successfully.");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.loginUser(loginRequest);

        return ResponseEntity.ok("Login Successfull : "+jwt);
    }
}
