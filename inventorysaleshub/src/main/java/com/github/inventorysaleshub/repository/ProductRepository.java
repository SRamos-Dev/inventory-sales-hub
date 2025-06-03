package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Product;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByStockLessThan(int stockThreshold);

}

