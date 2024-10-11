package com.project.parkrental.cart;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart") // 테이블 이름을 명시
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx; // idx 필드로 수정

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt; // added_at 필드

    @Column(nullable = false)
    private String username;

    @Column(name = "product_num", nullable = false) // nullable 속성 추가
    private String productNum;

    @Column(name = "productName", nullable = false) // column 이름과 nullable 속성 설정
    private String productName;

    @Column(name = "productPrice", nullable = false) // column 이름과 nullable 속성 설정
    private long productPrice; // BIGINT에 맞춰 long 타입으로 변경

    @Column(name = "quantity", nullable = false) // column 이름과 nullable 속성 설정
    private int quantity;

    // Getter 및 Setter 메서드
    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return (int) productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
