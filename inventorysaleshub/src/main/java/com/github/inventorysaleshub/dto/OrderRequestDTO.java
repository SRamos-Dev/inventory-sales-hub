package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OrderRequestDTO {

    @NotNull(message = "Items list cannot be null")
    private List<OrderItemRequestDTO> items; // List of products in the order
    public OrderRequestDTO() {
    }
    public OrderRequestDTO(List<OrderItemRequestDTO> items) {
        this.items = items;
    }

    // Getters and setters
    public List<OrderItemRequestDTO> getItems() {
        return items;
    }
    public void setItems(List<OrderItemRequestDTO> items) {
        this.items = items;
    }
}

