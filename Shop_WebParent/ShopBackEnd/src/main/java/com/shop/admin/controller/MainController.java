package com.shop.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String string() {
       return "login"; 
    }
}
