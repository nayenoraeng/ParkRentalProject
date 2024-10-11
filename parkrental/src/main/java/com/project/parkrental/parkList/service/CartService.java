package com.project.parkrental.parkList.service;

import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.parkList.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private List<Product> cartItems = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    // 장바구니에 제품 추가 (DB에서 가져온 제품)
    public void addProductsToCart(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        cartItems.addAll(products);
    }

    // 장바구니 아이템 가져오기
    public List<Product> getCartItems() {
        return cartItems;
    }

    // 장바구니에서 제품 제거 (추후 필요 시 구현)
    public void removeProductFromCart(Long productId) {
        cartItems.removeIf(product -> product.getId().equals(productId));
    }
}
