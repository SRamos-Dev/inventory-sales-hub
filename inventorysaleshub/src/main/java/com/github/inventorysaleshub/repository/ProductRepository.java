package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

}

