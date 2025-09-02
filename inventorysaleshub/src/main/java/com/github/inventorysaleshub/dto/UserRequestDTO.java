package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.*;

public class UserRequestDTO {

    @NotBlank(message = "User name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotNull(message = "Role ID cannot be null")
    private Long roleId; // The ID of the role to assign to the user

    // Getters and Setters
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

    public Long getRoleId() { 
        return roleId; 
    }
    public void setRoleId(Long roleId) { 
        this.roleId = roleId; 
    }
}

