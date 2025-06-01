package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Store name cannot be empty")
    @Size(min = 3, max = 100, message = "Store name must be between 3 and 100 characters")
    private String name; // Ensures name is properly formatted

    @NotBlank(message = "Location cannot be empty")
    @Size(min = 5, max = 150, message = "Location must be between 5 and 150 characters")
    private String location; // Restricts location field length

    @OneToMany(mappedBy = "store")
    private List<Product> products;
}
