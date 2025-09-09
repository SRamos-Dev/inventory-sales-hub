package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StoreRequestDTO {

    @NotBlank(message = "Store name cannot be empty")
    @Size(min = 3, max = 100, message = "Store name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String address;

    @NotBlank(message = "Phone cannot be empty")
    @Size(min = 7, max = 20, message = "Phone must be between 7 and 20 characters")
    private String phone;

    @Email(message = "Must be a valid email address")
    @Size(max = 120, message = "Email cannot exceed 120 characters")
    private String email;

    // --- Getters and Setters ---
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getAddress() { 
        return address; 
    }
    public void setAddress(String address) { 
        this.address = address; 
    }

    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
}

