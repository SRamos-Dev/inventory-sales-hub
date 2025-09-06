package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    // --- Sales summary (mock data) ---
    @GetMapping("/sales-summary")
    public ResponseEntity<ApiResponseDTO<SalesSummaryDTO>> getSalesSummary() {
        SalesSummaryDTO summary = new SalesSummaryDTO(
                120, // total orders
                new BigDecimal("45000.75"), // total revenue
                new BigDecimal("375.01") // average order value
        );
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Sales summary retrieved successfully", summary));
    }

    // --- Top products (mock data) ---
    @GetMapping("/top-products")
    public ResponseEntity<ApiResponseDTO<List<TopProductDTO>>> getTopProducts() {
        List<TopProductDTO> products = Arrays.asList(
                new TopProductDTO(1L, "Laptop", 50),
                new TopProductDTO(2L, "Smartphone", 75),
                new TopProductDTO(3L, "Headphones", 30)
        );
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Top products retrieved successfully", products));
    }

    // --- Top customers (mock data) ---
    @GetMapping("/top-customers")
    public ResponseEntity<ApiResponseDTO<List<TopCustomerDTO>>> getTopCustomers() {
        List<TopCustomerDTO> customers = Arrays.asList(
                new TopCustomerDTO(1L, "Alice Johnson", 10, 5000.0),
                new TopCustomerDTO(2L, "Bob Smith", 8, 3500.0),
                new TopCustomerDTO(3L, "Carlos Pérez", 6, 2900.0)
        );
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Top customers retrieved successfully", customers));
    }

    // --- Low stock products (mock data) ---
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponseDTO<List<LowStockProductDTO>>> getLowStockProducts() {
        List<LowStockProductDTO> lowStock = Arrays.asList(
                new LowStockProductDTO(4L, "Keyboard", 3),
                new LowStockProductDTO(5L, "Mouse", 2),
                new LowStockProductDTO(6L, "Monitor", 1)
        );
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Low stock products retrieved successfully", lowStock));
    }
}

