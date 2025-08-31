package com.github.inventorysaleshub.dto;

import jakarta.validation.constraints.*;

public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be greater than zero")
    private double price;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    // Getters y Setters
    public String getName() { 
        return name; 
    }
    public void setName(String name) {
        this.name = name; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
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

    public Long getCategoryId() { 
        return categoryId; 
    }
    public void setCategoryId(Long categoryId) { 
        this.categoryId = categoryId; 
    }
}
