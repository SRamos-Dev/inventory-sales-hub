package com.github.inventorysaleshub.model;

import jakarta.persistence.*;

@Entity
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method; // efectivo, tarjeta, etc.
    private String status; // pagado, pendiente, etc.

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
