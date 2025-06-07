package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "CASH|CARD|TRANSFER", message = "Payment method must be CASH, CARD, or TRANSFER")
    private String method; // Restricts allowed payment methods

    @NotBlank(message = "Payment status is required")
    @Pattern(regexp = "PENDING|PAID|REFUNDED|CANCELED", message = "Status must be PENDING, PAID, REFUNDED, or CANCELED")
    private String status; // Limits valid status values

    @OneToOne
    @JoinColumn(name = "order_id")
    @NotNull(message = "Payment must be associated with an order")
    private Order order; // Ensures every payment is linked to an order

    public Pay() {
    }

    public Pay(String method, String status, Order order) {
        this.method = method;
        this.status = status;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }
    
}
