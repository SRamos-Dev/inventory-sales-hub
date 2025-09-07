package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.*;
import com.github.inventorysaleshub.repository.OrderDetailsRepository;
import com.github.inventorysaleshub.repository.OrderRepository;
import com.github.inventorysaleshub.repository.ProductRepository;
import com.github.inventorysaleshub.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public DashboardController(OrderRepository orderRepository,
                               OrderDetailsRepository orderDetailsRepository,
                               ProductRepository productRepository,
                               UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // --- Sales summary (real data) ---
    @GetMapping("/sales-summary")
    public ResponseEntity<ApiResponseDTO<SalesSummaryDTO>> getSalesSummary() {
        long totalOrders = orderRepository.countTotalOrders();
        BigDecimal totalRevenue = orderRepository.calculateTotalRevenue();
        BigDecimal averageOrder = orderRepository.calculateAverageOrderValue();

        // Handle possible nulls if no orders exist yet
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        if (averageOrder == null) averageOrder = BigDecimal.ZERO;

        SalesSummaryDTO summary = new SalesSummaryDTO(totalOrders, totalRevenue, averageOrder);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Sales summary retrieved successfully", summary));
    }

    // --- Top products (real data) ---
    @GetMapping("/top-products")
    public ResponseEntity<ApiResponseDTO<List<TopProductDTO>>> getTopProducts() {
        List<TopProductDTO> products = orderDetailsRepository.findTopProducts();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Top products retrieved successfully", products));
    }

    // --- Top customers (real data) ---
    @GetMapping("/top-customers")
    public ResponseEntity<ApiResponseDTO<List<TopCustomerDTO>>> getTopCustomers() {
        List<TopCustomerDTO> customers = orderRepository.findTopCustomers();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Top customers retrieved successfully", customers));
    }

    // --- Low stock products (real data) ---
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponseDTO<List<LowStockProductDTO>>> getLowStockProducts() {
        List<LowStockProductDTO> lowStock = productRepository.findLowStockProducts();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Low stock products retrieved successfully", lowStock));
    }

    // --- Monthly sales (real data) ---
    @GetMapping("/monthly-sales")
    public ResponseEntity<ApiResponseDTO<List<MonthlySalesDTO>>> getMonthlySales() {
        List<MonthlySalesDTO> sales = orderRepository.getMonthlySales();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Monthly sales retrieved successfully", sales));
    }

    // --- Most profitable products (real data) ---
    @GetMapping("/most-profitable-products")
    public ResponseEntity<ApiResponseDTO<List<MostProfitableProductDTO>>> getMostProfitableProducts() {
        List<MostProfitableProductDTO> products = orderDetailsRepository.findMostProfitableProducts();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Most profitable products retrieved successfully", products));
    }

    // --- New customers per month (real data) ---
    @GetMapping("/new-customers")
    public ResponseEntity<ApiResponseDTO<List<NewCustomersDTO>>> getNewCustomers() {
        List<NewCustomersDTO> customers = userRepository.getNewCustomersPerMonth();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "New customers per month retrieved successfully", customers));
    }

}
