package com.github.inventorysaleshub.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(new ApiResponseDTO<>(true, "Order retrieved successfully",
                        modelMapper.map(order, OrderResponseDTO.class))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Order not found", null)));
    }

    // --- Create a new order ---
    @Operation(summary = "Create a new order", description = "Register a new order with products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
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
