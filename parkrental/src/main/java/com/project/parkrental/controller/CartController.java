package com.project.parkrental.controller;

import com.project.parkrental.cart.Cart;
import com.project.parkrental.cart.CartService;
import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.parkList.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/Cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

@PostMapping("/add")
public String addProductToCart(
        @RequestParam("idx[]") List<Long> idxList,  
        Model model) {
    try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        for (Long productIdx : idxList) {
            Product product = productService.getProductById(productIdx);

            if (product.getParkList() == null) {
                model.addAttribute("error", "공원 정보가 없습니다.");
                return "redirect:/user/Cart";
            }

            cartService.addProductToCart(username, product, 1);  // 수량을 기본 1로 설정
        }

        return "redirect:/user/Cart";

    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", e.getMessage());
        return "redirect:/user/Cart";
    }
}

    @GetMapping
    public String getCartProducts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;

        List<Cart> cartProducts = cartService.getCartProducts(username);
        cartProducts.forEach(cart -> {
            Product product = productService.getProductByNameAndParkId(cart.getProductName(), cart.getParkId());
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Product Price: " + cart.getProductPrice());
        });

        Long totalPrice = cartProducts.stream()
                .mapToLong(cart -> cart.getProductPrice() * cart.getQuantity())
                .sum();

        model.addAttribute("cartProducts", cartProducts);
        model.addAttribute("totalPrice", totalPrice);

        return "user/Cart";
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updateQuantity(
            @RequestBody List<Map<String, Object>> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            System.out.println("Received payload: " + payload);

            for (Map<String, Object> item : payload) {
                Long idx;
                Object idxObj = item.get("idx");

                if (idxObj instanceof Number) {
                    idx = ((Number) idxObj).longValue();
                } else if (idxObj instanceof String) {
                    idx = Long.parseLong((String) idxObj);
                } else {
                    throw new IllegalArgumentException("idx 값이 유효하지 않습니다: " + idxObj);
                }

                int quantity;
                Object quantityObj = item.get("quantity");

                if (quantityObj instanceof Number) {
                    quantity = ((Number) quantityObj).intValue();
                } else if (quantityObj instanceof String) {
                    quantity = Integer.parseInt((String) quantityObj);
                } else {
                    throw new IllegalArgumentException("quantity 값이 유효하지 않습니다: " + quantityObj);
                }

                cartService.updateQuantity(idx, quantity);
            }

            Long newTotalPrice = cartService.calculateTotalPrice(username);
            response.put("newTotalPrice", String.valueOf(newTotalPrice));
            response.put("success", "수량이 변경되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{idx}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long idx) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // 장바구니에서 해당 사용자의 항목인지 확인 후 삭제
            Cart cart = cartService.findByIdxAndUsername(idx, username);
            if (cart != null) {
                cartService.deleteProductFromCart(idx);
                response.put("success", "물품이 삭제되었습니다.");
            } else {
                response.put("error", "해당 항목을 삭제할 권한이 없습니다.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "물품을 삭제하는 도중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

}
