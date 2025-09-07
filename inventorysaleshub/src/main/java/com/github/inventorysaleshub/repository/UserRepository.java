package com.github.inventorysaleshub.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.inventorysaleshub.dto.NewCustomersDTO;
import com.github.inventorysaleshub.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // Derived query

    // New customers per month
    @Query("SELECT new com.github.inventorysaleshub.dto.NewCustomersDTO(" +
       "YEAR(u.dateCreated), MONTH(u.dateCreated), COUNT(u)) " +
       "FROM User u " +
       "GROUP BY YEAR(u.dateCreated), MONTH(u.dateCreated) " +
       "ORDER BY YEAR(u.dateCreated), MONTH(u.dateCreated)")
    List<NewCustomersDTO> getNewCustomersPerMonth();
}

