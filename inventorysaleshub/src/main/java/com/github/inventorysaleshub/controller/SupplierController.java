package com.github.inventorysaleshub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.inventorysaleshub.model.Supplier;
import com.github.inventorysaleshub.repository.SupplierRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    // Obtener todos los proveedores
    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // Crear nuevo proveedor
    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody @Valid Supplier supplier) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierRepository.save(supplier));
    }
}

