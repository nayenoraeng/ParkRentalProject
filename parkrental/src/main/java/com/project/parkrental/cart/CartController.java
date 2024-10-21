package com.project.parkrental.cart;

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

    // 장바구니에 상품을 추가하는 메소드
    @PostMapping("/add")
    public String addProductToCart(
            @RequestParam("idx[]") List<Long> idxList,
            @RequestParam("quantity[]") List<Integer> quantities, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            for (int i = 0; i < idxList.size(); i++) {
                Long productIdx = idxList.get(i);
                Product product = productService.getProductById(productIdx);

                if (product.getParkList() == null) {
                    model.addAttribute("error", "공원 정보가 없습니다.");
                    return "redirect:/user/Cart";
                }

                int quantity = quantities.get(i);  // 수량을 배열에서 가져옴

                // 장바구니에 상품을 추가
                cartService.addProductToCart(username, product, quantity);
            }

            // 장바구니 페이지로 리다이렉트
            return "redirect:/user/Cart";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "redirect:/user/Cart";  // 에러 발생 시에도 장바구니 페이지로 리다이렉트
        }
    }

    // 장바구니 상품을 가져오는 메소드
    @GetMapping
    public String getCartProducts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;

        List<Cart> cartProducts = cartService.getCartProducts(username);
        cartProducts.forEach(cart -> {
            Product product = productService.getProductByNameAndParkId(cart.getProductName(), cart.getParkId());
            System.out.println("Product ID: " + cart.getIdx()); // idx 값 확인
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

    // 수량 업데이트 메소드
    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updateQuantity(
            @RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            // 로그 추가: 전달된 payload 값 출력
            System.out.println("Received payload: " + payload);

            // Handle idx, check if it's String or Number
            Long idx;
            Object idxObj = payload.get("idx");
            System.out.println("전달된 idx 값: " + idxObj);

            if (idxObj instanceof String) {
                idx = Long.parseLong((String) idxObj); // Convert from String
            } else if (idxObj instanceof Number) {
                idx = ((Number) idxObj).longValue(); // Handle as Number
            } else {
                throw new IllegalArgumentException("idx 값이 없습니다.");
            }

            // Handle quantity, check if it's String or Number
            int quantity;
            Object quantityObj = payload.get("quantity");
            if (quantityObj instanceof String) {
                quantity = Integer.parseInt((String) quantityObj); // Convert from String
            } else if (quantityObj instanceof Number) {
                quantity = ((Number) quantityObj).intValue(); // Handle as Number
            } else {
                throw new IllegalArgumentException("quantity 값이 없습니다.");
            }

            // 수량 업데이트 로직 호출
            cartService.updateQuantity(idx, quantity);
            Long newTotalPrice = cartService.calculateTotalPrice();  // 전체 가격 계산
            response.put("newTotalPrice", String.valueOf(newTotalPrice));
            response.put("success", "수량이 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } catch (ClassCastException | NumberFormatException e) {
            e.printStackTrace();
            response.put("error", "잘못된 데이터 타입입니다. 수량은 숫자여야 합니다.");
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
