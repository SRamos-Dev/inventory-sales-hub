package com.github.inventorysaleshub.dto;

public class LowStockProductDTO {
    private Long productId;     // Product ID
    private String productName; // Product name
    private int stock;          // Current stock

    // Default constructor
    public LowStockProductDTO() {}

    // All-args constructor
    public LowStockProductDTO(Long productId, String productName, int stock) {
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
    }

    // Getters and setters
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}
