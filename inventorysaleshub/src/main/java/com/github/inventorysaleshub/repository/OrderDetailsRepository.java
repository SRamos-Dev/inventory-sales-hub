package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.dto.MostProfitableProductDTO;
import com.github.inventorysaleshub.dto.TopProductDTO;
import com.github.inventorysaleshub.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    // Custom query to find top products based on total quantity sold
    @Query("SELECT new com.github.inventorysaleshub.dto.TopProductDTO(" +
           "p.id, p.name, SUM(d.quantity)) " +
           "FROM OrderDetails d JOIN d.product p " +
           "GROUP BY p.id, p.name " +
           "ORDER BY SUM(d.quantity) DESC")
    List<TopProductDTO> findTopProducts();

    // Custom query to find most profitable products based on total revenue
    @Query("SELECT new com.github.inventorysaleshub.dto.MostProfitableProductDTO(" +
       "p.id, p.name, SUM(d.price * d.quantity)) " +
       "FROM OrderDetails d JOIN d.product p " +
       "GROUP BY p.id, p.name " +
       "ORDER BY SUM(d.price * d.quantity) DESC")
    List<MostProfitableProductDTO> findMostProfitableProducts();

}
