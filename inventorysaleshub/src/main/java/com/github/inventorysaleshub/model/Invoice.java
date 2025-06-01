package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "The date cannot be in the future")
    private LocalDate issueDate;

    @Positive(message = "Total amount must be greater than zero")
    private double totalAmount;

    @NotNull(message = "The invoice must be associated with an order")
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
