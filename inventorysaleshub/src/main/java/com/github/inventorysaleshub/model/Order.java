package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Creation date is required")
    @PastOrPresent(message = "The date cannot be in the future")
    private LocalDateTime dateCreated;

    @NotBlank(message = "Order status is required")
    @Pattern(regexp = "PENDING|PAID|SHIPPED|CANCELED", 
             message = "Status must be PENDING, PAID, SHIPPED, or CANCELED")
    private String status;

    @PositiveOrZero(message = "Total amount must be zero or greater")
    private double total;

    @NotNull(message = "The order must be associated with a user")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetails> details;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Invoice invoice;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Pay pay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public List<OrderDetails> getDetails() {
        return details;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Pay getPay() {
        return pay;
    }
    
}