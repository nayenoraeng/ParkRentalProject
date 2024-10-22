package com.project.parkrental.reservation;

import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.security.DTO.Seller;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, unique = true)
    private String reserveNum;

    @Column(nullable = false, unique = true)
    private LocalDate reserveDate;

    @Column(nullable = false, unique = true)
    private LocalTime reserveTime;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int isPaid; // 결제 상태 (1 = 결제 완료, 0 = 결제 안됨)

    @Column(nullable = false, unique = true)
    private Long costAll;

    // 연관된 판매자 (businessName 참조)
    @ManyToOne
    @JoinColumn(name = "businessName", referencedColumnName = "businessName", nullable = false)
    private Seller seller;

    // 연관된 상품 (productName 참조)
    @ManyToOne
    @JoinColumn(name = "productName", referencedColumnName = "productName", nullable = false)
    private Product product;
}
