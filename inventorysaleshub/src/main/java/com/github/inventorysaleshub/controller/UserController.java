package com.github.inventorysaleshub.controller;

import com.github.inventorysaleshub.model.Order;
import com.github.inventorysaleshub.model.User;
import com.github.inventorysaleshub.repository.OrderRepository;
import com.github.inventorysaleshub.repository.UserRepository;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<User> crearUsuario(@RequestBody @Valid User newUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(newUser));
    }

    // Obtener todos los usuarios (sólo para admin)
    @GetMapping
    public List<User> usersList() {
        return userRepository.findAll();
    }

    // Obtener perfil de usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener pedidos del usuario
    @GetMapping("/{id}/order")
    public ResponseEntity<List<Order>> getOrderByUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(orderRepository.findByUserId(id));
    }
}

