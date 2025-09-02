package com.github.inventorysaleshub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import com.github.inventorysaleshub.dto.InvoiceDTO;
import com.github.inventorysaleshub.dto.InvoiceRequestDTO;
import com.github.inventorysaleshub.model.Invoice;
import com.github.inventorysaleshub.model.Order;
import com.github.inventorysaleshub.repository.InvoiceRepository;
import com.github.inventorysaleshub.repository.OrderRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    public InvoiceController(InvoiceRepository invoiceRepository,
                             OrderRepository orderRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
    }

    // ------------------ CREATE ------------------
    @PostMapping
    public ResponseEntity<ApiResponseDTO<InvoiceDTO>> createInvoice(@Valid @RequestBody InvoiceRequestDTO request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId()));

        Invoice invoice = new Invoice();
        invoice.setIssueDate(request.getIssueDate());
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setOrder(order);

        Invoice saved = invoiceRepository.save(invoice);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Invoice created successfully", new InvoiceDTO(saved)));
    }

    // ------------------ READ ------------------
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<InvoiceDTO>> getInvoiceById(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(invoice -> ResponseEntity.ok(
                        new ApiResponseDTO<>(true, "Invoice found", new InvoiceDTO(invoice))
                ))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO<>(false, "Invoice not found", null)));
    }

    // ------------------ LIST ALL ------------------
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<InvoiceDTO>>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceRepository.findAll()
                .stream()
                .map(InvoiceDTO::new)
                .toList();

        return ResponseEntity.ok(new ApiResponseDTO<>(true, "All invoices retrieved", invoices));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Object>> deleteInvoice(@PathVariable Long id) {
        return invoiceRepository.findById(id).map(invoice -> {
            invoiceRepository.delete(invoice);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Invoice deleted successfully", null));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, "Invoice not found", null)));
    }
}


