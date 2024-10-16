package com.project.parkrental.parkList.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productName", "parkId"})
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @Column(name = "productNum", nullable = false, length = 10)
    private String productNum;

    @Column(name = "businessName", nullable = false, length = 100)
    private String businessName;

    @Column(name = "productName", nullable = false, length = 50)
    private String productName;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "cost")
    private Long cost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parkId", referencedColumnName = "parkId")
    private ParkList parkList;

    // 기본 생성자
    public Product() {}

    // 매개변수가 있는 생성자
    public Product(Long id, String productNum, String businessName, String productName, int quantity, Long cost, ParkList parkList) {
        this.id = id;
        this.productNum = productNum;
        this.businessName = businessName;
        this.productName = productName;
        this.quantity = quantity;
        this.cost = cost;
        this.parkList = parkList;
    }

    // parkId 값을 반환하는 getter
    public Long getParkId() {
        return this.parkList.getParkId();
    }
}
