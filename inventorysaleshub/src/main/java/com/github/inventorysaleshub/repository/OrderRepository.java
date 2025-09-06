package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

import com.github.inventorysaleshub.dto.TopCustomerDTO;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Count total orders
    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    // Sum of all order amounts
    @Query("SELECT SUM(d.price * d.quantity) FROM Order o JOIN o.details d")
    BigDecimal calculateTotalRevenue();

    // Average order value
    @Query("SELECT AVG(d.price * d.quantity) FROM Order o JOIN o.details d")
    BigDecimal calculateAverageOrderValue();

    

    @Query("SELECT new com.github.inventorysaleshub.dto.TopCustomerDTO(" +
        "u.id, u.name, COUNT(o), SUM(d.price * d.quantity)) " +
        "FROM Order o JOIN o.user u JOIN o.details d " +
        "GROUP BY u.id, u.name " +
        "ORDER BY SUM(d.price * d.quantity) DESC")
    List<TopCustomerDTO> findTopCustomers();
}
