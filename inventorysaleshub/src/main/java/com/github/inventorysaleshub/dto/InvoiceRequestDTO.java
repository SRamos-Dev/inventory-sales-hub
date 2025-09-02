package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class InvoiceRequestDTO {

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "The date cannot be in the future")
    private LocalDate issueDate;

    @Positive(message = "Total amount must be greater than zero")
    private double totalAmount;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    // --- Getters and setters ---
    public LocalDate getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}

