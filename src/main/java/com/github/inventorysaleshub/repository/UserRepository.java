package com.github.inventorysaleshub.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.inventorysaleshub.model.User;
import com.github.inventorysaleshub.model.dto.NewCustomersDTO;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // Derived query

    // New customers per month
    @Query("SELECT new com.github.inventorysaleshub.model.dto.NewCustomersDTO(" +
       "YEAR(u.createdAt), MONTH(u.createdAt), COUNT(u)) " +
       "FROM User u " +
       "GROUP BY YEAR(u.createdAt), MONTH(u.createdAt) " +
       "ORDER BY YEAR(u.createdAt), MONTH(u.createdAt)")
    List<NewCustomersDTO> getNewCustomersPerMonth();
}

