package com.github.inventorysaleshub.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import com.github.inventorysaleshub.dto.OrderRequestDTO;
import com.github.inventorysaleshub.dto.OrderResponseDTO;
import com.github.inventorysaleshub.model.Order;
import com.github.inventorysaleshub.model.OrderDetails;
import com.github.inventorysaleshub.model.Product;
import com.github.inventorysaleshub.repository.OrderRepository;
import com.github.inventorysaleshub.repository.ProductRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository,
                           ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all orders ---
    @Operation(summary = "Get all orders", description = "Retrieve all orders with details")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getAllOrders() {
        List<OrderResponseDTO> orders = orderRepository.findAll()
                .stream()
                .map(o -> modelMapper.map(o, OrderResponseDTO.class))
                .toList();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Orders retrieved successfully", orders));
    }

    // --- Get order by ID ---
    @Operation(summary = "Get an order by ID", description = "Retrieve a single order with details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - You cannot access this order"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getOrderById(
            @PathVariable Long id,
            Authentication authentication) {

        Optional<Order> orderOpt = orderRepository.findById(id);

        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Order not found", null));
        }

        Order order = orderOpt.get();

        // Get authenticated user email
        String currentUserEmail = authentication.getName();
        String orderOwnerEmail = order.getUser().getEmail();

        // Check if the user is ADMIN or the owner of the order
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !orderOwnerEmail.equals(currentUserEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDTO<>(false, "You are not allowed to access this order", null));
        }

        // If authorized, return the order
        OrderResponseDTO dto = modelMapper.map(order, OrderResponseDTO.class);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Order retrieved successfully", dto));
    }


    // --- Create a new order ---
    @Operation(summary = "Create a new order", description = "Register a new order with products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> createOrder(
            @Valid @RequestBody OrderRequestDTO request) {

        Order order = modelMapper.map(request, Order.class);

        List<OrderDetails> details = request.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + item.getProductId()));

            OrderDetails detail = new OrderDetails();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            return detail;
        }).toList();

        order.setOrderDetails(details);
        Order saved = orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Order created successfully",
                        modelMapper.map(saved, OrderResponseDTO.class)));
    }

    // --- Update an order ---
    @Operation(summary = "Update an order", description = "Update an existing order by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order updated successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDTO request) {

        return orderRepository.findById(id)
                .map(order -> {
                    modelMapper.map(request, order);
                    Order updated = orderRepository.save(order);
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Order updated successfully",
                            modelMapper.map(updated, OrderResponseDTO.class)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Order not found", null)));
    }

    // --- Delete an order ---
    @Operation(summary = "Delete an order", description = "Remove an order by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderRepository.delete(order);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "Order deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "Order not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}
