package com.project.parkrental.cart;

import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.parkList.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user/Cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String addProductToCart(@RequestParam("productIds") List<String> productIds,
                                   @RequestParam("username") String username) {
        for (String productId : productIds) {
            try {
                Long productIdLong = Long.parseLong(productId);
                Product product = productService.getProductById(productIdLong);

                // parkList와 parkId가 null인지 확인하는 로그
                if (product.getParkList() == null) {
                    System.out.println("Error: parkList is null for product ID: " + productIdLong);
                    return "redirect:/errorPage";  // 오류 페이지로 리다이렉트
                }

                Long parkId = product.getParkList().getParkId();
                System.out.println("Park ID: " + parkId);  // parkId가 제대로 설정되었는지 로그 확인

                String productNum = product.getProductNum();
                String productName = product.getProductName();
                Long productPrice = product.getCost();
                int quantity = 1;

                cartService.addProductToCart(username, productNum, productName, productPrice, parkId, quantity);
            } catch (NumberFormatException e) {
                System.out.println("잘못된 productId: " + productId);
                return "redirect:/errorPage";
            }
        }

        return "redirect:/user/Cart";
    }

    @GetMapping
    public String getCartProducts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Cart> cartProducts = cartService.getCartProducts(username);

        // 로그로 parkList가 제대로 들어오는지 확인
        cartProducts.forEach(cart -> {
            if (cart.getProduct() != null && cart.getProduct().getParkList() != null) {
                System.out.println("Park Name: " + cart.getProduct().getParkList().getParkNm());
            } else {
                System.out.println("ParkList is null for product: " + cart.getProduct().getProductName());
            }
        });

        model.addAttribute("cartProducts", cartProducts);
        return "user/Cart";
    }
}
