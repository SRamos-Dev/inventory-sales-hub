package com.github.inventorysaleshub.dto;

import java.time.LocalDate;

import com.github.inventorysaleshub.model.Invoice;

public class InvoiceDTO {
    private Long id;
    private LocalDate issueDate;
    private double totalAmount;
    private Long orderId;

    public InvoiceDTO(Invoice invoice) {
        this.id = invoice.getId();
        this.issueDate = invoice.getIssueDate();
        this.totalAmount = invoice.getTotalAmount();
        this.orderId = invoice.getOrder().getId();
    }

    //Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

