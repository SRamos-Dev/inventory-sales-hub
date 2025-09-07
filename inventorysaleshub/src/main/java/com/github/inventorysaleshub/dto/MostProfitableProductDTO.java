package com.github.inventorysaleshub.dto;

public class MostProfitableProductDTO {
    private Long productId;       // Product ID
    private String productName;   // Product name
    private double totalRevenue;  // Total revenue generated

    // Default constructor
    public MostProfitableProductDTO() {}

    // All-args constructor
    public MostProfitableProductDTO(Long productId, String productName, double totalRevenue) {
        this.productId = productId;
        this.productName = productName;
        this.totalRevenue = totalRevenue;
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

    public double getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}

