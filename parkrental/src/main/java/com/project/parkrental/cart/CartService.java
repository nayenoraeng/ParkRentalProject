package com.project.parkrental.cart;

import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.parkList.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // 장바구니에 제품 추가 (동일 제품이 있으면 수량 업데이트, 아니면 새로 추가)
    public void addProductToCart(String username, String productNum, String productName, Long productPrice, Long parkId, int quantity) {
        Cart existingCart = cartRepository.findByUsernameAndProductNumAndParkId(username, productNum, parkId);

        if (existingCart != null) {
            // 이미 카트에 존재하는 제품이 있다면 수량만 증가
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            cartRepository.save(existingCart);
        } else {
            Optional<Product> optionalProduct = productRepository.findProductWithParkList(productNum, parkId);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();  // product를 가져옴

                // 새 Cart 객체 생성 후 product 설정
                Cart newCart = new Cart();
                newCart.setUsername(username);
                newCart.setProduct(product);
                newCart.setQuantity(quantity);
                cartRepository.save(newCart);  // 카트 저장
            } else {
                // 예외 처리 또는 적절한 로직 (예: 상품을 찾을 수 없는 경우)
                throw new RuntimeException("해당 제품을 찾을 수 없습니다.");
            }
        }
    }

    // 장바구니에서 제품 제거
    public void removeProductFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    // 장바구니 항목 목록 가져오기
    public List<Cart> getCartProducts(String username) {
        return cartRepository.findByUsername(username);
    }

}
