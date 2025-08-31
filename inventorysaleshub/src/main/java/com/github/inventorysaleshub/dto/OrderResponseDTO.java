package com.github.inventorysaleshub.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderResponseDTO {
    private Long id;
    private LocalDate orderDate;
    private String status;

    private List<OrderItemDTO> items;   // Product list
    private InvoiceDTO invoice;         // Associated invoice
    private PayDTO payment;             // Associated payment

    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }
    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public PayDTO getPayment() {
        return payment;
    }
    public void setPayment(PayDTO payment) {
        this.payment = payment;
    }
    public List<OrderItemDTO> getItems() {
        return items;
    }
    
}
