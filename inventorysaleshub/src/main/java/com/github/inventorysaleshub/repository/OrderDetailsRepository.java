package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

}
