package com.github.inventorysaleshub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import com.github.inventorysaleshub.dto.PayDTO;
import com.github.inventorysaleshub.dto.PayRequestDTO;
import com.github.inventorysaleshub.model.Order;
import com.github.inventorysaleshub.model.Pay;
import com.github.inventorysaleshub.repository.OrderRepository;
import com.github.inventorysaleshub.repository.PayRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PayController {

    private final PayRepository payRepository;
    private final OrderRepository orderRepository;

    public PayController(PayRepository payRepository,
                         OrderRepository orderRepository) {
        this.payRepository = payRepository;
        this.orderRepository = orderRepository;
    }

    // ------------------ CREATE ------------------
    @PostMapping
    public ResponseEntity<ApiResponseDTO<PayDTO>> createPayment(@Valid @RequestBody PayRequestDTO request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId()));

        Pay pay = new Pay();
        pay.setMethod(request.getMethod());
        pay.setStatus(request.getStatus());
        pay.setOrder(order);

        Pay saved = payRepository.save(pay);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Payment created successfully", new PayDTO(saved)));
    }

    // ------------------ READ ------------------
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PayDTO>> getPaymentById(@PathVariable Long id) {
        return payRepository.findById(id)
                .map(pay -> ResponseEntity.ok(
                        new ApiResponseDTO<>(true, "Payment found", new PayDTO(pay))
                ))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Payment not found", null)));
    }

    // ------------------ LIST ALL ------------------
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PayDTO>>> getAllPayments() {
        List<PayDTO> payments = payRepository.findAll()
                .stream()
                .map(PayDTO::new)
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "All payments retrieved", payments));
    }

    // ------------------ UPDATE STATUS ------------------
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseDTO<PayDTO>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return payRepository.findById(id).map(pay -> {
            pay.setStatus(status);
            Pay updated = payRepository.save(pay);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payment status updated", new PayDTO(updated)));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "Payment not found", null)));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> deletePayment(@PathVariable Long id) {
        return payRepository.findById(id).map(pay -> {
            payRepository.delete(pay);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Payment deleted successfully", null));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "Payment not found", null)));
    }
}

