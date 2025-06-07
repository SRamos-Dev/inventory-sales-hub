package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.*;

public class UserRequestDTO {

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role ID is required")
    private Long roleId;

    // Getters y Setters
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

