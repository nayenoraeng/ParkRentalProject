package com.project.parkrental.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserpageController {

    private final UserService userService;

    public UserpageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userpage")
    public String userpage(Model model){
        UserDto user = userService.getUserDetails();
        model.addAttribute("user", user);
        return "user/userpage";
    }

    @GetMapping("/userpageEdit")
    public String userpageEdit(Model model) {
        UserDto user = userService.getUserDetails();
        model.addAttribute("user", user);
        return "user/userpageEdit";
    }

    @PostMapping("/userpageEdit")
    public String editUser(@ModelAttribute("user") UserDto userDto, BindingResult result, Principal principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("User authenticated: " + SecurityContextHolder.getContext().getAuthentication().getName());


        if (principal == null) {
            System.out.println("principal is null");
        }
        if (result.hasErrors()) {
            return "user/userpageEdit";
        }

        System.out.println("username?: " + username);
        User existingUser = userService.findByUsername(username);
        if (existingUser == null) {
            throw new RuntimeException("로그인된 사용자를 찾을 수 없습니다.");
        }

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNum(userDto.getPhoneNum());
        existingUser.setPostcode(userDto.getPostcode());
        existingUser.setAddress(userDto.getAddress());
        existingUser.setDetailAddress(userDto.getDetailAddress());

        userService.updateUser(existingUser);

        return "redirect:/user/userpage";
    }
}
