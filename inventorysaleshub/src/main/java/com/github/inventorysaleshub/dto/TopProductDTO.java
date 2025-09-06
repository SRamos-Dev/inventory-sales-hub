package com.github.inventorysaleshub.dto;

public class TopProductDTO {
    private Long productId;     // Product ID
    private String productName; // Product name
    private long quantitySold;  // Total quantity sold

    // Default constructor
    public TopProductDTO() {}

    // All-args constructor
    public TopProductDTO(Long productId, String productName, long quantitySold) {
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
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

    public long getQuantitySold() {
        return quantitySold;
    }
    public void setQuantitySold(long quantitySold) {
        this.quantitySold = quantitySold;
    }
}

