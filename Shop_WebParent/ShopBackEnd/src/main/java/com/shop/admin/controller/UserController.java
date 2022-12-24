package com.shop.admin.controller;

import com.shop.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/users")
    public String findAll(Model model) {
        model.addAttribute("users", service.findAll());
        return "users";
    }



}
