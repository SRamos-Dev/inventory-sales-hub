package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int stock;

    // Relación con Category (Muchos productos -> Una categoría)
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Relación con Suplier
    @ManyToOne
    @JoinColumn(name = "suplier_id")
    private Suplier suplier;

    // Relación con Store
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    // Relación con OrderDetails
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails;

    // Relación con ProductHistory
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductHistory> history;
}
