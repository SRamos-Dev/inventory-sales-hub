package com.github.inventorysaleshub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.inventorysaleshub.model.Pay;
import com.github.inventorysaleshub.repository.PayRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pays")
public class PayController {

    @Autowired
    private PayRepository payRepository;

    // Registrar pago
    @PostMapping
    public ResponseEntity<Pay> payRegister(@RequestBody @Valid Pay pay) {
        return ResponseEntity.status(HttpStatus.CREATED).body(payRepository.save(pay));
    }

    // Obtener pago por pedido
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Pay> obtenerPagoPorPedido(@PathVariable Long orderId) {
        return payRepository.findByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
