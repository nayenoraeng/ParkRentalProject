package com.project.parkrental.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void addToCart(String username, String productNum, String productName, long productPrice, int quantity, LocalDateTime addedAt) {
        CartItem cartItem = new CartItem();
        cartItem.setUsername(username);
        cartItem.setProductNum(productNum); // productNum 설정
        cartItem.setProductName(productName);
        cartItem.setProductPrice(productPrice);
        cartItem.setQuantity(quantity);
        cartItem.setAddedAt(LocalDateTime.now()); // 현재 시간으로 addedAt 설정

        cartRepository.save(cartItem); // cartRepository는 JPA Repository
    }

    public List<CartItem> getCartItems(String username) {
        List<CartItem> cartItems = cartRepository.findByUsername(username);
        System.out.println(cartItems); // 콘솔에 cartItems 출력
        return cartItems;
    }

    public int calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream().mapToInt(item -> item.getProductPrice() * item.getQuantity()).sum();
    }

    public int calculateFinalPrice(List<CartItem> cartItems) {
        // Implement any discounts or other logic here
        return calculateTotalPrice(cartItems);
    }

    public void removeFromCart(Long id) {
    }
}


