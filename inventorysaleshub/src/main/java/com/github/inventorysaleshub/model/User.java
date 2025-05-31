package com.github.inventorysaleshub.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Relación con Role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // Relación con Order
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

}

