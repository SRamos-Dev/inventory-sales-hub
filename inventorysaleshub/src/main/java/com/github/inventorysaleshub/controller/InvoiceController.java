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
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<InvoiceDTO>> getInvoiceById(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(invoice -> ResponseEntity.ok(new ApiResponseDTO<>(true, "Invoice retrieved successfully",
                        modelMapper.map(invoice, InvoiceDTO.class))))
                .orElseGet(() -> {
                    ApiResponseDTO<InvoiceDTO> response = new ApiResponseDTO<>(false, "Invoice not found", null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    // --- Create a new invoice ---
    @Operation(summary = "Create a new invoice", description = "Register a new invoice linked to an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invoice created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
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
