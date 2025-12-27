package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PayRepository extends JpaRepository<Pay, Long> {

    // This finds a Pay entity by its related Order ID
    Optional<Pay> findByOrderId(Long orderId);

    // --- Average Payment Time (Native SQL) ---
    // This calculates the average difference in days between payment creation and order creation within a date range
    @Query(value = "SELECT AVG(DATEDIFF(p.date_created, o.date_created)) " +
                   "FROM payments p " +
                   "JOIN orders o ON p.order_id = o.id " +
                   "WHERE o.date_created BETWEEN :from AND :to",
           nativeQuery = true)
    Double calculateAveragePaymentTime(@Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);
}
