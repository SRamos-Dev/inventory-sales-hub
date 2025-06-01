package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name cannot be empty")
    @Size(min = 3, max = 100, message = "Company name must be between 3 and 100 characters")
    private String companyName; // Ensures name is properly formatted

    @NotBlank(message = "Contact information cannot be empty")
    @Size(min = 10, max = 150, message = "Contact info must be between 10 and 150 characters")
    private String contactInfo; // Restricts contact details length

    @OneToMany(mappedBy = "suplier")
    private List<Product> products;
}

