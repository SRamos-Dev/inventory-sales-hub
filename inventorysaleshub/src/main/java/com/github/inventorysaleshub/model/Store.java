package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "stores")
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

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public List<Product> getProducts() {
        return products;
    }
    
}
