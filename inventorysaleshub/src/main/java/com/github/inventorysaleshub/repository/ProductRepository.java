package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Product;
import com.github.inventorysaleshub.dto.LowStockProductDTO;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByStockLessThan(int stockThreshold);
    
    @Query("SELECT new com.github.inventorysaleshub.dto.LowStockProductDTO(" +
       "p.id, p.name, p.stock) " +
       "FROM Product p WHERE p.stock < 5")
    List<LowStockProductDTO> findLowStockProducts();    
}


