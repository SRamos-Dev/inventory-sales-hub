package com.github.inventorysaleshub.dto;

import com.github.inventorysaleshub.model.Pay;

public class PayDTO {
    private Long id;
    private String method;
    private String status;
    private Long orderId;

    public PayDTO(Pay pay) {
        this.id = pay.getId();
        this.method = pay.getMethod();
        this.status = pay.getStatus();
        this.orderId = pay.getOrder().getId();
    }

    // Getters y setters
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
}

