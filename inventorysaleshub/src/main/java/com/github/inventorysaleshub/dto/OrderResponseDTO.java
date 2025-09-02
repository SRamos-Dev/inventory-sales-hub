package com.github.inventorysaleshub.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.github.inventorysaleshub.model.Order;

public class OrderResponseDTO {
    private Long id;
    private LocalDateTime dateCreated;
    private String status;

    private List<OrderItemDTO> items;
    private InvoiceDTO invoice;
    private PayDTO payment;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.dateCreated = order.getDateCreated();
        this.status = order.getStatus();

        // Convert details to DTOs
        this.items = order.getOrderDetails()
                .stream()
                .map(OrderItemDTO::new)
                .toList();

        // Invoice (if any)
        if (order.getInvoice() != null) {
            this.invoice = new InvoiceDTO(order.getInvoice());
        }

        // Pay (if any)
        if (order.getPay() != null) {
            this.payment = new PayDTO(order.getPay());
        }
    }
}

