package com.project.parkrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping(value = "/")
    public String Home(Model model)
    {
        model.addAttribute("name", "Hello World!");
        return "guest/Main";
    }
}
