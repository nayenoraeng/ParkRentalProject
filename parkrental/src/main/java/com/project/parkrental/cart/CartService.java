package com.project.parkrental.cart;

import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.parkList.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addProductToCart(String username, Product product, int quantity) {
        // 장바구니에서 이미 해당 상품이 있는지 확인
        Cart existingCart = cartRepository.findByUsernameAndProduct(username, product);

        int availableQuantity = product.getQuantity();

        // 장바구니에 이미 해당 상품이 있는 경우 수량을 추가
        if (existingCart != null) {
            int totalQuantity = existingCart.getQuantity() + quantity;
            if (totalQuantity > availableQuantity) {
                throw new RuntimeException("재고가 부족합니다. 최대 " + availableQuantity + "개까지 추가할 수 있습니다.");
            }
            existingCart.setQuantity(totalQuantity);
            cartRepository.save(existingCart);
        } else {
            // 장바구니에 없으면 새로운 항목을 추가
            if (quantity > availableQuantity) {
                throw new RuntimeException("재고가 부족합니다. 최대 " + availableQuantity + "개까지 추가할 수 있습니다.");
            }
            Cart newCart = new Cart();
            newCart.setUsername(username);
            newCart.setProductNum(product.getProductNum());
            newCart.setProductName(product.getProductName());
            newCart.setProductPrice(product.getCost());
            newCart.setQuantity(quantity);
            newCart.setParkId(product.getParkId());

            cartRepository.save(newCart);
        }
    }

    public List<Cart> getCartProducts(String username) {
        return cartRepository.findByUsername(username);
    }

    public void updateQuantity(Long idx, int newQuantity) {
        Cart cart = cartRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다. idx: " + idx));

        Product product = productRepository.findById(cart.getProduct().getIdx())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. 상품명: " + cart.getProduct().getProductName() + ", 공원 ID: " + cart.getParkId()));

        int availableQuantity = product.getQuantity();
        if (newQuantity > availableQuantity) {
            throw new RuntimeException("재고가 부족합니다. 최대 " + availableQuantity + "개까지 설정할 수 있습니다.");
        }

        cart.setQuantity(newQuantity);
        cartRepository.save(cart);
    }

    // 전체 장바구니 가격 계산
    public Long calculateTotalPrice() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Cart> cartProducts = cartRepository.findByUsername(username);

        return cartProducts.stream()
                .mapToLong(cart -> cart.getProductPrice() * cart.getQuantity())
                .sum();
    }
}
