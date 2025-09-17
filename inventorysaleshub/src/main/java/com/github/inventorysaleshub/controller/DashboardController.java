package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.dto.*;
import com.github.inventorysaleshub.repository.OrderDetailsRepository;
import com.github.inventorysaleshub.repository.OrderRepository;
import com.github.inventorysaleshub.repository.PayRepository;
import com.github.inventorysaleshub.repository.ProductRepository;
import com.github.inventorysaleshub.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PayRepository payRepository;

    public DashboardController(OrderRepository orderRepository,
                               OrderDetailsRepository orderDetailsRepository,
                               ProductRepository productRepository,
                               UserRepository userRepository,
                               PayRepository payRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.payRepository = payRepository;
    }

    // --- Sales summary (real data) ---
    @Operation(summary = "Get sales summary", description = "Retrieve total orders, revenue and average order value")
    @ApiResponse(responseCode = "200", description = "Sales summary retrieved successfully")
    @GetMapping("/sales-summary")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDTO<SalesSummaryDTO>> getSalesSummary() {
        long totalOrders = orderRepository.countTotalOrders();
        BigDecimal totalRevenue = orderRepository.calculateTotalRevenue();
        BigDecimal averageOrder = orderRepository.calculateAverageOrderValue();

        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        if (averageOrder == null) averageOrder = BigDecimal.ZERO;

        SalesSummaryDTO summary = new SalesSummaryDTO(totalOrders, totalRevenue, averageOrder);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Sales summary retrieved successfully", summary));
    }

    // --- Top products (real data) ---
    @Operation(summary = "Get top products", description = "Retrieve best-selling products")
    @ApiResponse(responseCode = "200", description = "Top products retrieved successfully")
    @GetMapping("/top-products")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponseDTO<List<TopProductDTO>>> getTopProducts() {
        List<TopProductDTO> products = orderDetailsRepository.findTopProducts();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Top products retrieved successfully", products));
    }

    // --- Top customers (real data) ---
    @Operation(summary = "Get top customers", description = "Retrieve customers with the highest purchases")
    @ApiResponse(responseCode = "200", description = "Top customers retrieved successfully")
    @GetMapping("/top-customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<TopCustomerDTO>>> getTopCustomers() {
        List<TopCustomerDTO> customers = orderRepository.findTopCustomers();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Top customers retrieved successfully", customers));
    }

    // --- Low stock products (real data) ---
    @Operation(summary = "Get low stock products", description = "Retrieve products with low inventory")
    @ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully")
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<LowStockProductDTO>>> getLowStockProducts() {
        List<LowStockProductDTO> lowStock = productRepository.findLowStockProducts();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Low stock products retrieved successfully", lowStock));
    }

    // --- Monthly sales (real data) ---
    @Operation(summary = "Get monthly sales", description = "Retrieve sales grouped by month")
    @ApiResponse(responseCode = "200", description = "Monthly sales retrieved successfully")
    @GetMapping("/monthly-sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<MonthlySalesDTO>>> getMonthlySales() {
        List<MonthlySalesDTO> sales = orderRepository.getMonthlySales();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Monthly sales retrieved successfully", sales));
    }

    // --- Most profitable products (real data) ---
    @Operation(summary = "Get most profitable products", description = "Retrieve products with highest profit margin")
    @ApiResponse(responseCode = "200", description = "Most profitable products retrieved successfully")
    @GetMapping("/most-profitable-products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<MostProfitableProductDTO>>> getMostProfitableProducts() {
        List<MostProfitableProductDTO> products = orderDetailsRepository.findMostProfitableProducts();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Most profitable products retrieved successfully", products));
    }

    // --- New customers per month (real data) ---
    @Operation(summary = "Get new customers per month", description = "Retrieve how many new customers registered per month")
    @ApiResponse(responseCode = "200", description = "New customers retrieved successfully")
    @GetMapping("/new-customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<NewCustomersDTO>>> getNewCustomers() {
        List<NewCustomersDTO> customers = userRepository.getNewCustomersPerMonth();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "New customers per month retrieved successfully", customers));
    }

    // ==========================
    // 📊 FILTERED BY DATE RANGE
    // ==========================

    // --- Sales summary with date range ---
    @Operation(summary = "Get sales summary by date range", description = "Retrieve orders, revenue and average order value within a date range")
    @ApiResponse(responseCode = "200", description = "Sales summary retrieved successfully")
    @GetMapping("/sales-summary/filter")
    public ResponseEntity<ApiResponseDTO<SalesSummaryDTO>> getSalesSummaryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        long totalOrders = orderRepository.countOrdersByDateRange(from, to);
        BigDecimal totalRevenue = orderRepository.calculateRevenueByDateRange(from, to);
        BigDecimal averageOrder = orderRepository.calculateAverageOrderValueByDateRange(from, to);

        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        if (averageOrder == null) averageOrder = BigDecimal.ZERO;

        SalesSummaryDTO summary = new SalesSummaryDTO(totalOrders, totalRevenue, averageOrder);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Sales summary (filtered) retrieved successfully", summary));
    }

    // --- Top customers with date range ---
    @Operation(summary = "Get top customers by date range", description = "Retrieve customers with highest purchases within a date range")
    @ApiResponse(responseCode = "200", description = "Top customers retrieved successfully")
    @GetMapping("/top-customers/filter")
    public ResponseEntity<ApiResponseDTO<List<TopCustomerDTO>>> getTopCustomersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        List<Object[]> rows = orderRepository.findTopCustomersByDateRangeRaw(from, to);

        // Clear mapping with constructor
        List<TopCustomerDTO> customers = rows.stream()
                .map(r -> new TopCustomerDTO(
                        ((Number) r[0]).longValue(),  // customerId
                        (String) r[1],               // customerName
                        ((Number) r[2]).longValue(), // ordersCount
                        ((Number) r[3]).doubleValue()// totalSpent
                ))
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Top customers (filtered) retrieved successfully", customers));
    }

    // --- Monthly sales with date range ---
    @Operation(summary = "Get monthly sales by date range", description = "Retrieve sales grouped by month within a date range")
    @ApiResponse(responseCode = "200", description = "Monthly sales retrieved successfully")
    @GetMapping("/monthly-sales/filter")
    public ResponseEntity<ApiResponseDTO<List<MonthlySalesDTO>>> getMonthlySalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        List<Object[]> rows = orderRepository.getMonthlySalesBetweenRaw(from, to);

        // Clear mapping with constructor
        List<MonthlySalesDTO> sales = rows.stream()
                .map(r -> new MonthlySalesDTO(
                        ((Number) r[0]).intValue(),  // year
                        ((Number) r[1]).intValue(),  // month
                        ((Number) r[2]).doubleValue()// totalSales
                ))
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Monthly sales (filtered) retrieved successfully", sales));
    }


    // --- Extra KPIs in date range ---
    @Operation(summary = "Get extra KPIs", description = "Retrieve advanced KPIs for sales performance in a date range")
    @ApiResponse(responseCode = "200", description = "KPIs retrieved successfully")
    @GetMapping("/kpis")
    public ResponseEntity<ApiResponseDTO<List<KpiDTO>>> getExtraKpis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        List<KpiDTO> kpis = new ArrayList<>();

        Double retention = orderRepository.calculateCustomerRetentionRate(from, to);
        Double frequency = orderRepository.calculateAverageOrderFrequency(from, to);
        Double margin = orderRepository.calculateGrossProfitMargin(from, to);
        Double paymentTime = payRepository.calculateAveragePaymentTime(from, to);

        kpis.add(new KpiDTO("Customer Retention Rate (%)", retention != null ? retention : 0.0));
        kpis.add(new KpiDTO("Average Order Frequency", frequency != null ? frequency : 0.0));
        kpis.add(new KpiDTO("Gross Profit Margin (%)", margin != null ? margin : 0.0));
        kpis.add(new KpiDTO("Average Payment Time (days)", paymentTime != null ? paymentTime : 0.0));

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "KPIs retrieved successfully", kpis));
    }
}


