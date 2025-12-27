package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Invoice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByOrderId(Long orderId);

}
