package com.project.parkrental.security.Controller;

import com.project.parkrental.security.DTO.Seller;
import com.project.parkrental.security.DTO.User;
import com.project.parkrental.security.Service.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/guest")
@Controller
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @GetMapping("/sellerSignup")
    public String signup (Model model) {
        model.addAttribute("seller", new Seller());
        return "guest/sellerSignup";
    }

    @PostMapping("/checkSellerName")
    @ResponseBody
    public String checkUsername(@RequestParam("username") String username) {
        System.out.println("AJAX 요청 수신 - Username: " + username);
        boolean exist = sellerService.isUsernameTaken(username);
        return exist? "이미 사용중인 아이디입니다.":"사용가능한 아이디입니다.";
    }

//    @PostMapping("/checkSellerEmail")
//    @ResponseBody
//    public String checkEmail(@RequestParam("email") String email) {
//        boolean exist = sellerService.isEmailTaken(email);
//        return exist? "이미 사용중인 이메일입니다.":"사용가능한 이메일입니다.";
//    }
//
//    @PostMapping("/checkSellerPhoneNum")
//    @ResponseBody
//    public String checkPhoneNum(@RequestParam("phoneNum") String phoneNum) {
//        boolean exist = sellerService.isPhoneNumTaken(phoneNum);
//        return exist? "이미 사용중인 전화번호입니다.":"사용가능한 전화번호입니다.";
//    }

    @PostMapping("/sellerSignup")
    public String registerUser(@Valid @ModelAttribute Seller seller, BindingResult result, Model model) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach(violation -> {
                System.out.println("error: " + violation.getDefaultMessage());
            });
            return "guest/sellerSignup";
        }

        if (!seller.getPassword().equals(seller.getConfirmPassword())) {
            model.addAttribute("passwordError", "비밀번호가 일치하지 않습니다.");
            return "guest/sellerSignup";
        }

        sellerService.registerNewUser(seller);
        return "redirect:/guest/Login";
    }
}
