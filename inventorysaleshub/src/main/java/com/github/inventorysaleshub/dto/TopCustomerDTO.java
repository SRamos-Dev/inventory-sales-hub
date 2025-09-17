package com.github.inventorysaleshub.dto;

public class TopCustomerDTO {
    private Long customerId;     // User ID
    private String customerName; // Customer name
    private long ordersCount;    // Number of orders placed
    private double totalSpent;   // Total amount spent

    // Default constructor
    public TopCustomerDTO() {}

    // All-args constructor
    public TopCustomerDTO(Long customerId, String customerName, long ordersCount, double totalSpent) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.ordersCount = ordersCount;
        this.totalSpent = totalSpent;
    }

    public TopCustomerDTO(Long customerId, String customerName, long ordersCount) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.ordersCount = ordersCount;
    this.totalSpent = 0; // default
    }

    // Getters and setters
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getOrdersCount() {
        return ordersCount;
    }
    public void setOrdersCount(long ordersCount) {
        this.ordersCount = ordersCount;
    }

    public double getTotalSpent() {
        return totalSpent;
    }
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
}

