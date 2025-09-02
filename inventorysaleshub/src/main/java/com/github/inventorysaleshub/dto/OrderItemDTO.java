package com.github.inventorysaleshub.dto;

import com.github.inventorysaleshub.model.OrderDetails;

public class OrderItemDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double price;

    public OrderItemDTO(OrderDetails detail) {
        this.productId = detail.getProduct().getId();
        this.productName = detail.getProduct().getName();
        this.quantity = detail.getQuantity();
        this.price = detail.getPrice();
    }
}

