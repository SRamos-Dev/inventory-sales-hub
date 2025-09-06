package com.github.inventorysaleshub.dto;

import java.math.BigDecimal;

public class SalesSummaryDTO {
    private long totalOrders;        // Total number of orders
    private BigDecimal totalRevenue; // Total revenue generated
    private BigDecimal averageOrder; // Average order value

    // Default constructor
    public SalesSummaryDTO() {}

    // All-args constructor
    public SalesSummaryDTO(long totalOrders, BigDecimal totalRevenue, BigDecimal averageOrder) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.averageOrder = averageOrder;
    }

    // Getters and setters
    public long getTotalOrders() {
        return totalOrders;
    }
    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAverageOrder() {
        return averageOrder;
    }
    public void setAverageOrder(BigDecimal averageOrder) {
        this.averageOrder = averageOrder;
    }
}
