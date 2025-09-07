package com.github.inventorysaleshub.dto;

public class MonthlySalesDTO {
    private int year;          // Year of sales
    private int month;         // Month of sales
    private double totalSales; // Total revenue in that month

    // Default constructor
    public MonthlySalesDTO() {}

    // All-args constructor
    public MonthlySalesDTO(int year, int month, double totalSales) {
        this.year = year;
        this.month = month;
        this.totalSales = totalSales;
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

    public double getTotalSales() {
        return totalSales;
    }
    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
}

