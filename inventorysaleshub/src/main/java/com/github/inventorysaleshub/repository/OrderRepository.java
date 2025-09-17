package com.github.inventorysaleshub.repository;

import com.github.inventorysaleshub.model.Order;
import com.github.inventorysaleshub.dto.MonthlySalesDTO;
import com.github.inventorysaleshub.dto.TopCustomerDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // =======================
    // BASIC KPIs (JPQL)
    // =======================

    // Returns total number of orders
    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    // Returns total revenue computed from order details (price * quantity)
    @Query("SELECT SUM(d.price * d.quantity) FROM Order o JOIN o.details d")
    BigDecimal calculateTotalRevenue();

    // Returns average order value computed from order details
    @Query("SELECT AVG(d.price * d.quantity) FROM Order o JOIN o.details d")
    BigDecimal calculateAverageOrderValue();


    // =======================
    // TOP CUSTOMERS & MONTHLY SALES (GLOBAL) (JPQL)
    // =======================

    // Returns a list of TopCustomerDTO using a constructor expression
    @Query("SELECT new com.github.inventorysaleshub.dto.TopCustomerDTO(" +
           "u.id, u.name, COUNT(o), SUM(d.price * d.quantity)) " +
           "FROM Order o JOIN o.user u JOIN o.details d " +
           "GROUP BY u.id, u.name " +
           "ORDER BY SUM(d.price * d.quantity) DESC")
    List<TopCustomerDTO> findTopCustomers();

    // Returns month/year/total sales using MonthlySalesDTO(year, month, total)
    @Query("SELECT new com.github.inventorysaleshub.dto.MonthlySalesDTO(" +
           "YEAR(o.dateCreated), MONTH(o.dateCreated), SUM(d.price * d.quantity)) " +
           "FROM Order o JOIN o.details d " +
           "GROUP BY YEAR(o.dateCreated), MONTH(o.dateCreated) " +
           "ORDER BY YEAR(o.dateCreated), MONTH(o.dateCreated)")
    List<MonthlySalesDTO> getMonthlySales();


    // =======================
    // FILTERS BY DATE RANGE (Native SQL)
    // =======================

    // Returns total number of orders in a date range
    @Query(value = "SELECT COUNT(*) FROM orders o " +
                   "WHERE o.date_created BETWEEN :from AND :to", nativeQuery = true)
    Long countOrdersByDateRange(@Param("from") LocalDateTime from,
                                @Param("to") LocalDateTime to);

    // Returns total revenue in a date range
    @Query(value = "SELECT SUM(d.price * d.quantity) FROM orders o " +
                   "JOIN order_details d ON o.id = d.order_id " +
                   "WHERE o.date_created BETWEEN :from AND :to", nativeQuery = true)
    BigDecimal calculateRevenueByDateRange(@Param("from") LocalDateTime from,
                                           @Param("to") LocalDateTime to);

    // Returns average order value in a date range
    @Query(value = "SELECT AVG(d.price * d.quantity) FROM orders o " +
                   "JOIN order_details d ON o.id = d.order_id " +
                   "WHERE o.date_created BETWEEN :from AND :to", nativeQuery = true)
    BigDecimal calculateAverageOrderValueByDateRange(@Param("from") LocalDateTime from,
                                                     @Param("to") LocalDateTime to);

    // Returns raw data for Top Customers filtered by date range
    @Query(value = "SELECT u.id as customerId, u.name as customerName, COUNT(o.id) as ordersCount, " +
                   "SUM(d.price * d.quantity) as totalSpent " +
                   "FROM orders o " +
                   "JOIN users u ON o.user_id = u.id " +
                   "JOIN order_details d ON o.id = d.order_id " +
                   "WHERE o.date_created BETWEEN :from AND :to " +
                   "GROUP BY u.id, u.name " +
                   "ORDER BY totalSpent DESC", nativeQuery = true)
    List<Object[]> findTopCustomersByDateRangeRaw(@Param("from") LocalDateTime from,
                                                  @Param("to") LocalDateTime to);

    // Returns raw data for Monthly Sales filtered by date range
    @Query(value = "SELECT YEAR(o.date_created) as year, MONTH(o.date_created) as month, " +
                   "SUM(d.price * d.quantity) as totalSales " +
                   "FROM orders o " +
                   "JOIN order_details d ON o.id = d.order_id " +
                   "WHERE o.date_created BETWEEN :from AND :to " +
                   "GROUP BY YEAR(o.date_created), MONTH(o.date_created) " +
                   "ORDER BY YEAR(o.date_created), MONTH(o.date_created)", nativeQuery = true)
    List<Object[]> getMonthlySalesBetweenRaw(@Param("from") LocalDateTime from,
                                             @Param("to") LocalDateTime to);


    // =======================
    // EXTRA KPIs (Native SQL)
    // =======================

    // Returns customer retention rate in a date range
    @Query(value = "SELECT (COUNT(DISTINCT o.user_id) * 1.0 / (SELECT COUNT(*) FROM users)) * 100 " +
                   "FROM orders o WHERE o.date_created BETWEEN :from AND :to", nativeQuery = true)
    Double calculateCustomerRetentionRate(@Param("from") LocalDateTime from,
                                          @Param("to") LocalDateTime to);

    // Returns average order frequency per customer in a date range
    @Query(value = "SELECT AVG(order_count) FROM (" +
                   "SELECT COUNT(*) as order_count FROM orders o " +
                   "WHERE o.date_created BETWEEN :from AND :to GROUP BY o.user_id) as sub",
           nativeQuery = true)
    Double calculateAverageOrderFrequency(@Param("from") LocalDateTime from,
                                          @Param("to") LocalDateTime to);

    // Returns gross profit margin in a date range (requires cost_price in products)
    @Query(value = "SELECT ((SUM(d.price * d.quantity) - SUM(p.cost_price * d.quantity)) / SUM(d.price * d.quantity)) * 100 " +
                   "FROM orders o " +
                   "JOIN order_details d ON o.id = d.order_id " +
                   "JOIN products p ON d.product_id = p.id " +
                   "WHERE o.date_created BETWEEN :from AND :to", nativeQuery = true)
    Double calculateGrossProfitMargin(@Param("from") LocalDateTime from,
                                      @Param("to") LocalDateTime to);
}
