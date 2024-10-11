package com.project.parkrental.cart;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody CartItem cartItem) {
        try {
            // productNum이 null이거나 빈 값이면 에러 처리
            if (cartItem.getProductNum() == null || cartItem.getProductNum().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid product number!"));
            }

            // addedAt 필드를 현재 시간으로 설정
            cartItem.setAddedAt(LocalDateTime.now());

            // 장바구니에 상품 추가
            cartService.addToCart(
                    cartItem.getUsername(),
                    cartItem.getProductNum(),
                    cartItem.getProductName(),
                    cartItem.getProductPrice(),
                    cartItem.getQuantity(),
                    cartItem.getAddedAt() // 추가된 시간도 넘김
            );
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            e.printStackTrace(); // 예외 정보를 콘솔에 출력하여 디버깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("success", false));
        }
    }

    @GetMapping
    public String viewCart(@RequestParam String username, Model model) {
        List<CartItem> cartItems = cartService.getCartItems(username);
        model.addAttribute("cartItems", cartItems);
        return "cart"; // 장바구니 뷰 반환
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart"; // 장바구니로 리다이렉트
    }


}