package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name cannot be null")
    private String name;

    @NotBlank(message = "A description is required")
    @Size(min = 20, message = "The description must be at least 20 characters long")
    private String description;

    @PositiveOrZero(message = "The price must be zero or greater")
    private double price;

    @Min(value = 1, message = "There must be at least 1 product in stock")
    private int stock;

    // Relationship with Category (Many products -> One category)
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Relationship with Supplier
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    // Relationship with Store
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    // Relationship with OrderDetails
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails;

    // Relationship with ProductHistory
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductHistory> history;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
