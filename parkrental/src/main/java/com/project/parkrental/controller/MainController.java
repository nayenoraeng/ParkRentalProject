package com.project.parkrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/")
    public String Home(Model model)
    {
        model.addAttribute("name", "Hello World!");
        return "guest/Main";
    }
    @GetMapping("/guest/rantal")
    public String showRantalPage(Model model) {
        // 필요한 데이터나 정보를 Model에 추가하여 View에 전달
        model.addAttribute("title", "물품 대여 페이지");

        // 물품 목록 등의 데이터를 추가할 수 있음
        // 예시: model.addAttribute("rentalItems", rentalService.getAllItems());

        return "guest/rantal"; // guest/rantal.html 템플릿을 렌더링
    }
    @GetMapping("/guest/cart") // "/guest/cart" 요청을 처리
    public String cartPage(Model model) {
        model.addAttribute("title", "장바구니");
        // 예를 들어, 장바구니에 담긴 상품 목록을 가져와서 모델에 추가할 수 있습니다.
        // String username = "user1"; // 로그인 후 동적으로 변경 가능
        // List<CartItem> cartItems = cartService.getCartItems(username);
        // model.addAttribute("cartItems", cartItems);

        return "guest/cart"; // "guest/cart.html"을 렌더링
    }


}
