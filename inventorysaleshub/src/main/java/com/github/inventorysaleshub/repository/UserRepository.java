package com.github.inventorysaleshub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.inventorysaleshub.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
