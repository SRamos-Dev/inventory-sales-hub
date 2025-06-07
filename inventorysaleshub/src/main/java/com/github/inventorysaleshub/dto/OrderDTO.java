package com.github.inventorysaleshub.dto;

import java.time.LocalDateTime;

import com.github.inventorysaleshub.model.Order;

public class OrderDTO {
    private Long id;
    private LocalDateTime dateCreated;
    private String status;
    private double total;
    private String userEmail;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.dateCreated = order.getDateCreated();
        this.status = order.getStatus();
        this.total = order.getTotal();
        this.userEmail = order.getUser().getEmail();
    }

    // Getters y setters
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
}

