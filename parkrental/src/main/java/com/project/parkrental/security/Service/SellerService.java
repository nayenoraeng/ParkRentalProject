package com.project.parkrental.security.Service;

import com.project.parkrental.security.DTO.Seller;
import com.project.parkrental.security.DTO.SellerDto;
import com.project.parkrental.security.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerNewUser (Seller seller) {
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        seller.setAuthority("ROLE_SELLER");
        System.out.println("save method called");
        sellerRepository.save(seller);
    }

    public boolean isUsernameTaken (String username) {
        return sellerRepository.existsByUsername(username);
    }

    public Seller findByUsername(String username) {
        return sellerRepository.findByUsername(username);
    }

    public SellerDto getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("사용자가 인증되지 않았습니다.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByUsername(username);

        return new SellerDto(seller);
    }

    public void updateUser(Seller seller) {
        sellerRepository.save(seller);
    }
}
