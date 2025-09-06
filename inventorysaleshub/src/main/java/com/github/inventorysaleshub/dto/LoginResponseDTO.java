package com.github.inventorysaleshub.dto;

public class LoginResponseDTO {

    private String token;   // JWT token
    private String email;   // User email
    private String role;    // User role

    public LoginResponseDTO(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    // --- Getters ---
    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}

