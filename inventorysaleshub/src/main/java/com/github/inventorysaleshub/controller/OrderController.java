package com.github.inventorysaleshub.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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

    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // ------------------ CREATE ------------------
    @Operation(summary = "Create a new order", description = "Register a new order with products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = OrderResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        Order order = new Order();
        order.setDateCreated(LocalDateTime.now());
        order.setStatus("PENDING");

        // Build order details from request items
        List<OrderDetails> details = request.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product with ID " + item.getProductId() + " not found"));

            OrderDetails detail = new OrderDetails();
            detail.setOrder(order); // Link back to order
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            return detail;
        }).toList();

        // Link details to order
        order.setOrderDetails(details);

        // Calculate total
        double total = details.stream().mapToDouble(d -> d.getPrice() * d.getQuantity()).sum();
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Order created successfully", new OrderResponseDTO(savedOrder)));
    }


    // ------------------ READ (by ID) ------------------
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(
                        new ApiResponseDTO<>(true, "Order found", new OrderResponseDTO(order))
                ))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Order not found", null)));
    }

    // ------------------ LIST ALL ------------------
    @Operation(summary = "Get all orders", description = "Retrieve all orders with details, invoice and payment")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponseDTO.class)))
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getAllOrders() {
        List<OrderResponseDTO> orders = orderRepository.findAll()
                .stream()
                .map(OrderResponseDTO::new)
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "All orders retrieved", orders));
    }

    // ------------------ UPDATE STATUS ------------------
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return orderRepository.findById(id).map(order -> {
            order.setStatus(status);
            Order updated = orderRepository.save(order);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Order status updated", new OrderResponseDTO(updated)));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "Order not found", null)));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> deleteOrder(@PathVariable Long id) {
        return orderRepository.findById(id).map(order -> {
            orderRepository.delete(order);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Order deleted successfully", null));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "Order not found", null)));
    }
}


