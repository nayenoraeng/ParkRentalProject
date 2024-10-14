package com.project.parkrental.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
