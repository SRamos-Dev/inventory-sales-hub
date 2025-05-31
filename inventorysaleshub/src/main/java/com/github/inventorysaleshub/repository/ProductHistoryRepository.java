package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {

}