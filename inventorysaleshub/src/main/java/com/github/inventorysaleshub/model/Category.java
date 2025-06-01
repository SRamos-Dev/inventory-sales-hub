package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
