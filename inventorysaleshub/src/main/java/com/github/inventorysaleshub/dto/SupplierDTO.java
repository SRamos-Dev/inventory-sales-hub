package com.github.inventorysaleshub.dto;

public class SupplierDTO {

    private Long id;
    private String name;
    private String contact;
    private String email;
    private String phone;

    // --- Getters and Setters ---
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

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

