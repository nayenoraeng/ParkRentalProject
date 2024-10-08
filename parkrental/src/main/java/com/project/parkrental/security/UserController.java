package com.project.parkrental.security;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("guest")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @GetMapping("/Signup")
    public String signup (Model model) {
        model.addAttribute("user", new User());
        return "guest/Signup";
    }

    @PostMapping("/checkUsername")
    @ResponseBody
    public String checkUsername(@RequestParam("username") String username) {
        System.out.println("AJAX 요청 수신 - Username: " + username);
        boolean exist = userService.isUsernameTaken(username);
        return exist? "이미 사용중인 아이디입니다.":"사용가능한 아이디입니다.";
    }

    @PostMapping("/checkEmail")
    @ResponseBody
    public String checkEmail(@RequestParam("email") String email) {
        boolean exist = userService.isEmailTaken(email);
        return exist? "이미 사용중인 이메일입니다.":"사용가능한 이메일입니다.";
    }

    @PostMapping("/checkPhoneNum")
    @ResponseBody
    public String checkPhoneNum(@RequestParam("phoneNum") String phoneNum) {
        boolean exist = userService.isPhoneNumTaken(phoneNum);
        return exist? "이미 사용중인 전화번호입니다.":"사용가능한 전화번호입니다.";
    }

    @PostMapping("/Signup")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        System.out.println("typed password: " + user.getPassword());
        System.out.println("Result:" + result);
        if (result.hasErrors()) {
            result.getAllErrors().forEach(violation -> {
                System.out.println("error: " + violation.getDefaultMessage());
            });
            return "guest/Signup";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "비밀번호가 일치하지 않습니다.");
        }
        System.out.println("Received password: " + user.getPassword());
        userService.registerNewUser(user);
        return "redirect:/guest/Login";
    }

    @PostMapping("Login")
    public String authUser(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/home";
    }

    @Bean(name = "userPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
