package com.project.parkrental.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUsernameAndProductNumAndParkId(String username, String productNum, Long parkId);
    List<Cart> findByUsername(String username);
}
