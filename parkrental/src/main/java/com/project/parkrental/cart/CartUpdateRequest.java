package com.project.parkrental.cart;

import lombok.Data;

@Data
public class CartUpdateRequest {
    private Long idx;
    private int quantity;

    public CartUpdateRequest() {}

    public CartUpdateRequest(Long idx, int quantity) {
        this.idx = idx;
        this.quantity = quantity;
    }
}
