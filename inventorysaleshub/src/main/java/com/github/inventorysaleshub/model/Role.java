package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // Ej: "ADMIN", "USER"

    @OneToMany(mappedBy = "role")
    private List<User> users;

}
