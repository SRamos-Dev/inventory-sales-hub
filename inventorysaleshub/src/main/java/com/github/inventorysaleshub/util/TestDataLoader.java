
package com.github.inventorysaleshub.util;

import com.github.inventorysaleshub.model.*;
import com.github.inventorysaleshub.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Component
@Profile("test") // This will only run in 'test' profile
public class TestDataLoader {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private PayRepository payRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PostConstruct
    public void loadTestData() {
        // ----- Create roles -----
        Role adminRole = new Role("ADMIN_TEST");
        Role userRole = new Role("USER_TEST");
        roleRepository.saveAll(List.of(adminRole, userRole));

        // ----- Create users -----
        User admin = new User("Test Admin", "admin@test.com");
        admin.setRole(adminRole);

        User user = new User("Test User", "user@test.com");
        user.setRole(userRole);

        userRepository.saveAll(List.of(admin, user));

        // ----- Create products -----
        Product p1 = new Product("Test Laptop", "Laptop for testing", 1500.0, 10);
        Product p2 = new Product("Test Mouse", "Wireless mouse", 25.0, 50);

        productRepository.saveAll(List.of(p1, p2));

        // ----- Create test order -----
        Order order = new Order(LocalDateTime.now(), "COMPLETED", 1525.0, user);
        orderRepository.save(order);

        // ----- Create order details -----
        OrderDetails d1 = new OrderDetails(1, 1500.0, order, p1);
        OrderDetails d2 = new OrderDetails(1, 25.0, order, p2);
        orderDetailsRepository.saveAll(List.of(d1, d2));

        // ----- Simulate payment -----
        Pay pay = new Pay("Card", "Paid", order);
        payRepository.save(pay);

        // ----- Generate invoice -----
        Invoice invoice = new Invoice(LocalDate.now(), 1525.0, order);
        invoiceRepository.save(invoice);

        System.out.println("Test mode enabled: sample data loaded successfully.");
    }
}

