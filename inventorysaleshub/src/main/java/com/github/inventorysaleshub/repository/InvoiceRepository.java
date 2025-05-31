package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
