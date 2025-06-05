package com.github.inventorysaleshub.util;

import com.github.inventorysaleshub.model.*;
import com.github.inventorysaleshub.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("test") // Solo se ejecuta en modo prueba
public class TestDataLoader {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void loadTestData() {
        // Crear roles
        Role adminRole = new Role("ADMIN_TEST");
        Role userRole = new Role("USER_TEST");
        roleRepository.saveAll(List.of(adminRole, userRole));

        // Crear usuarios
        User admin = new User("Admin Prueba", "admin@test.com");
        admin.setRole(adminRole);

        User user = new User("Usuario Prueba", "usuario@test.com");
        user.setRole(userRole);

        userRepository.saveAll(List.of(admin, user));

        // Crear productos
        Product p1 = new Product("Laptop Prueba", "Laptop para pruebas", 1500.0, 10);
        Product p2 = new Product("Mouse Prueba", "Mouse inalámbrico", 25.0, 50);

        productRepository.saveAll(List.of(p1, p2));

        System.out.println("Modo prueba activado: Datos cargados correctamente.");
    }
}
