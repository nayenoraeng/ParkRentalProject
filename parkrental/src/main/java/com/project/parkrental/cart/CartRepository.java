package com.project.parkrental.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    // You can add custom query methods if needed
    List<CartItem> findByUsername(String username);
}