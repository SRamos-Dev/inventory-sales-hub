package com.github.inventorysaleshub.dto;

import java.time.LocalDateTime;

import com.github.inventorysaleshub.model.ProductHistory;

public class ProductHistoryDTO {
    private String action;
    private LocalDateTime timestamp;

    public ProductHistoryDTO(ProductHistory history) {
        this.action = history.getAction();
        this.timestamp = history.getTimestamp();
    }

    // Getters
    
    public String getAction() {
        return action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

