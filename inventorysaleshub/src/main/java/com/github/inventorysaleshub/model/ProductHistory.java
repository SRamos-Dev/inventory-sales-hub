package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Action type cannot be empty")
    @Pattern(regexp = "CREATED|UPDATED|DELETED|PRICE_CHANGED|STOCK_MODIFIED", message = "Action must be one of: CREATED, UPDATED, DELETED, PRICE_CHANGED, STOCK_MODIFIED")
    private String action; // Restricts possible values

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp; // Ensures valid date tracking

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product must be associated")
    private Product product; // Guarantees every history entry is linked to a product
}

