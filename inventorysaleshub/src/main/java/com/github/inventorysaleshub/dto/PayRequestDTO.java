package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class PayRequestDTO {

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "CASH|CARD|TRANSFER", message = "Payment method must be CASH, CARD, or TRANSFER")
    private String method;

    @NotBlank(message = "Payment status is required")
    @Pattern(regexp = "PENDING|PAID|REFUNDED|CANCELED", 
             message = "Status must be PENDING, PAID, REFUNDED, or CANCELED")
    private String status;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    // --- Getters and setters ---
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}

