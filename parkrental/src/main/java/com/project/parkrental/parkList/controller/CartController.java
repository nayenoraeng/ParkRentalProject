package com.project.parkrental.parkList.controller;

import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.parkList.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    // 물품 장바구니에 추가
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("productIds") List<Long> productIds, Principal principal, Model model) {
        // 로그인 여부 확인 (principal이 null이면 guest)
        if (principal == null) {
            model.addAttribute("errorMessage", "회원만 이용 가능한 서비스입니다.");
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }

        cartService.addProductsToCart(productIds);  // 장바구니에 제품 추가
        return "redirect:/cart";  // 장바구니 페이지로 리디렉션
    }

    // 장바구니 페이지
    @GetMapping("/cart")
    public String showCart(Model model) {
        List<Product> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        return "user/Cart";
    }
}
