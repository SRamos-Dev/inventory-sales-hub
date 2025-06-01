package com.github.inventorysaleshub.model;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name; // Ensures valid length and prevents empty values

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Must be a valid email address")
    private String email; // Ensures valid email format

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "User must have a role")
    private Role role; // Guarantees every user is assigned a role

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
}

