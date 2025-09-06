package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.*;

public class RegisterDTO {

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @NotNull(message = "Role ID cannot be null")
    private Long roleId; // Assigned role for the user

    // --- Getters and setters ---
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}

