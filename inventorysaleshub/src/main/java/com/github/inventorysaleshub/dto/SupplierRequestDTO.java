package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SupplierRequestDTO {

    @NotBlank(message = "Supplier name cannot be empty")
    @Size(min = 3, max = 100, message = "Supplier name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Contact cannot be empty")
    @Size(min = 3, max = 100, message = "Contact must be between 3 and 100 characters")
    private String contact;

    @Email(message = "Must be a valid email address")
    @Size(max = 120, message = "Email cannot exceed 120 characters")
    private String email;

    @NotBlank(message = "Phone cannot be empty")
    @Size(min = 7, max = 20, message = "Phone must be between 7 and 20 characters")
    private String phone;

    // --- Getters and Setters ---
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getContact() { 
        return contact; 
    }
    public void setContact(String contact) { 
        this.contact = contact; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getPhone() { 
        return phone;
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
}

