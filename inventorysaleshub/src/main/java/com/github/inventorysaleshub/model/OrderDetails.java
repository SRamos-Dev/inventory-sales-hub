package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;  // Ensures a minimum quantity of 1

    @DecimalMin(value = "0.1", message = "Price must be greater than 0")
    private double price;  // Prevents zero or negative prices

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NotNull(message = "Order cannot be null")
    private Order order;  // Ensures every order detail belongs to an order

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = "Product cannot be null")
    private Product product;  // Guarantees the existence of an associated product

    public OrderDetails() {
    }

    public OrderDetails(int quantity, double price, Order order, Product product) {
    this.quantity = quantity;
    this.price = price;
    this.order = order;
    this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }
    
}
