package com.github.inventorysaleshub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProductHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action; // ejemplo: "precio modificado", "stock ajustado"
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
