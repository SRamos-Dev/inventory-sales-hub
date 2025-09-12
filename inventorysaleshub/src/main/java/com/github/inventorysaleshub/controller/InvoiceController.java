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
import com.github.inventorysaleshub.dto.InvoiceDTO;
import com.github.inventorysaleshub.dto.InvoiceRequestDTO;
import com.github.inventorysaleshub.model.Invoice;
import com.github.inventorysaleshub.repository.InvoiceRepository;
import com.github.inventorysaleshub.repository.OrderRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public InvoiceController(InvoiceRepository invoiceRepository,
                             OrderRepository orderRepository,
                             ModelMapper modelMapper) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    // --- Get all invoices ---
    @Operation(summary = "Get all invoices", description = "Retrieve all invoices")
    @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<InvoiceDTO>>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceRepository.findAll()
                .stream()
                .map(i -> modelMapper.map(i, InvoiceDTO.class))
                .toList();
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Invoices retrieved successfully", invoices));
    }

    // --- Get invoice by ID ---
    @Operation(summary = "Get an invoice by ID", description = "Retrieve a single invoice by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<InvoiceDTO>> getInvoiceById(
            @PathVariable Long id,
            Authentication authentication) {

        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);

        if (invoiceOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Invoice not found", null));
        }

        Invoice invoice = invoiceOpt.get();

        String currentUserEmail = authentication.getName();
        String ownerEmail = invoice.getOrder().getUser().getEmail();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !ownerEmail.equals(currentUserEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseDTO<>(false, "You are not allowed to access this invoice", null));
        }

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "Invoice retrieved successfully",
                modelMapper.map(invoice, InvoiceDTO.class)));
    }

    // --- Create a new invoice ---
    @Operation(summary = "Create a new invoice", description = "Register a new invoice linked to an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invoice created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<InvoiceDTO>> createInvoice(
            @Valid @RequestBody InvoiceRequestDTO request) {

        Invoice invoice = modelMapper.map(request, Invoice.class);

        if (request.getOrderId() != null) {
            invoice.setOrder(orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId())));
        }

        Invoice saved = invoiceRepository.save(invoice);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Invoice created successfully",
                        modelMapper.map(saved, InvoiceDTO.class)));
    }

    // --- Update an invoice ---
    @Operation(summary = "Update an invoice", description = "Update an existing invoice by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice updated successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<InvoiceDTO>> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceRequestDTO request) {

        return invoiceRepository.findById(id)
                .map(invoice -> {
                    modelMapper.map(request, invoice);

                    if (request.getOrderId() != null) {
                        invoice.setOrder(orderRepository.findById(request.getOrderId())
                                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId())));
                    }

                    Invoice updated = invoiceRepository.save(invoice);
                    return ResponseEntity.ok(new ApiResponseDTO<>(true, "Invoice updated successfully",
                            modelMapper.map(updated, InvoiceDTO.class)));
                })
                .orElseGet(() -> {
                    ApiResponseDTO<InvoiceDTO> response = new ApiResponseDTO<>(false, "Invoice not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    // --- Delete an invoice ---
    @Operation(summary = "Delete an invoice", description = "Remove an invoice by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteInvoice(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(invoice -> {
                    invoiceRepository.delete(invoice);
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(true, "Invoice deleted successfully", null);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<Void> response = new ApiResponseDTO<>(false, "Invoice not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}

