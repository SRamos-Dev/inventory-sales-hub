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
import com.github.inventorysaleshub.dto.PayDTO;
import com.github.inventorysaleshub.dto.PayRequestDTO;
import com.github.inventorysaleshub.model.Pay;
import com.github.inventorysaleshub.repository.OrderRepository;
import com.github.inventorysaleshub.repository.PayRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PayController {

    private final PayRepository payRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public PayController(PayRepository payRepository,
                         OrderRepository orderRepository,
                         ModelMapper modelMapper) {
        this.payRepository = payRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all payments ---
    @Operation(summary = "Get all payments", description = "Retrieve all registered payments")
    @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PayDTO>>> getAllPayments() {
        List<PayDTO> payments = payRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PayDTO.class))
                .toList();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payments retrieved successfully", payments));
    }

    // --- Get payment by ID ---
    @Operation(summary = "Get a payment by ID", description = "Retrieve a single payment by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PayDTO>> getPaymentById(
            @PathVariable Long id,
            Authentication authentication) {

        Optional<Pay> payOpt = payRepository.findById(id);

        if (payOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Payment not found", null));
        }

        Pay pay = payOpt.get();
        String currentUserEmail = authentication.getName();
        String ownerEmail = pay.getOrder().getUser().getEmail();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !ownerEmail.equals(currentUserEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDTO<>(false, "You are not allowed to access this payment", null));
        }

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payment retrieved successfully",
                modelMapper.map(pay, PayDTO.class)));
    }

    // --- Create a new payment ---
    @Operation(summary = "Create a new payment", description = "Register a new payment linked to an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<PayDTO>> createPayment(
            @Valid @RequestBody PayRequestDTO request) {

        Pay pay = modelMapper.map(request, Pay.class);

        if (request.getOrderId() != null) {
            pay.setOrder(orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId())));
        }

        Pay saved = payRepository.save(pay);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Payment created successfully",
                        modelMapper.map(saved, PayDTO.class)));
    }

    // --- Update a payment ---
    @Operation(summary = "Update a payment", description = "Update an existing payment by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment updated successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PayDTO>> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PayRequestDTO request) {

        return payRepository.findById(id)
                .map(pay -> {
                    modelMapper.map(request, pay);

                    if (request.getOrderId() != null) {
                        pay.setOrder(orderRepository.findById(request.getOrderId())
                                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId())));
                    }

                    Pay updated = payRepository.save(pay);
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payment updated successfully",
                            modelMapper.map(updated, PayDTO.class)));
                })
                .orElseGet(() -> {
                    ApiResponseDTO<PayDTO> response = new ApiResponseDTO<>(false, "Payment not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    // --- Delete a payment ---
    @Operation(summary = "Delete a payment", description = "Remove a payment by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deletePayment(@PathVariable Long id) {
        return payRepository.findById(id)
                .map(pay -> {
                    payRepository.delete(pay);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "Payment deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "Payment not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}

