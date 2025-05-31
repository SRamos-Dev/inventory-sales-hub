package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}