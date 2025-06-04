package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Pay;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {

    Optional<Pay> findByOrderId(Long orderId);
}
