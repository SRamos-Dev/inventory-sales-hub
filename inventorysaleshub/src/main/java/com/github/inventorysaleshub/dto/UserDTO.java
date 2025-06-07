package com.github.inventorysaleshub.dto;

import com.github.inventorysaleshub.model.User;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String roleName; // en lugar de devolver todo el objeto Role

    // Constructor
    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roleName = user.getRole().getName();
    }

    // Getters y setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
}

