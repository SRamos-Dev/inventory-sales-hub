package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.*;
import com.github.inventorysaleshub.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // --- Register endpoint ---
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<UserDTO>> register(@Valid @RequestBody RegisterDTO request) {
        UserDTO savedUser = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "User registered successfully", savedUser));
    }

    // --- Login endpoint ---
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Login successful", response));
    }
}

