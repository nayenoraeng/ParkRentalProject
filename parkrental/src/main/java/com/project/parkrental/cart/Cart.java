package com.project.parkrental.cart;

import com.project.parkrental.parkList.model.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "productNum", nullable = false)
    private String productNum;

    @Column(name = "productName", nullable = false)
    private String productName;

    @Column(name = "productPrice", nullable = false)
    private Long productPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

    @ManyToOne
    @JoinColumn(name = "productNum", referencedColumnName = "productNum", insertable = false, updatable = false)
    private Product product;

    @Column(name = "parkId", nullable = false)
    private Long parkId; // parkId는 여전히 존재

}
