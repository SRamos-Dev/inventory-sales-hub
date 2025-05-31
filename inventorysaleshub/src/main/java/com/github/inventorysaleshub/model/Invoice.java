package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate issueDate;
    private double totalAmount;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
