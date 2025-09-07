package com.github.inventorysaleshub.dto;

public class NewCustomersDTO {
    private int year;        // Year of registration
    private int month;       // Month of registration
    private long totalUsers; // Total users registered in that month

    // Default constructor
    public NewCustomersDTO() {}

    // All-args constructor
    public NewCustomersDTO(int year, int month, long totalUsers) {
        this.year = year;
        this.month = month;
        this.totalUsers = totalUsers;
    }

    // Getters and setters
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public long getTotalUsers() {
        return totalUsers;
    }
    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }
}
