package com.github.inventorysaleshub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.inventorysaleshub.model.Store;
import com.github.inventorysaleshub.repository.StoreRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    // Obtener todas las sucursales
    @GetMapping
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // Crear nueva sucursal
    @PostMapping
    public ResponseEntity<Store> createStore(@RequestBody @Valid Store store) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeRepository.save(store));
    }
}

