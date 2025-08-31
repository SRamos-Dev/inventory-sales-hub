package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.*;

public class ProductUpdateDTO {
    @NotBlank
    private String name;

    @Positive
    private double price;

    @Min(0)
    private int stock;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}
