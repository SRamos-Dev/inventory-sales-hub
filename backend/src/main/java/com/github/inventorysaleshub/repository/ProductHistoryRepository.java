package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.ProductHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {

    List<ProductHistory> findByProductId(Long productId);

}