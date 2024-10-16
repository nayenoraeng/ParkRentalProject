package com.project.parkrental.security.Repository;

import com.project.parkrental.security.DTO.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNum(String phoneNum);
}
