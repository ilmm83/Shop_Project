package com.shop.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.service.UserService;
import com.shop.model.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", service.findAllUsers());
        return "users";
    }

    @GetMapping("/user_form")
    public String signIn(Model model) {
        User user = new User();
        user.setEnabled(true);

        model.addAttribute("roles", service.findAllRoles());
        model.addAttribute("user", user);
        return "user_form";
    }

    @PostMapping("/create")
    public String createNewUser(@ModelAttribute User user, RedirectAttributes redirect) {
        service.save(user);
        redirect.addFlashAttribute("message", "The user has been saved successfuly.");
        return "redirect:/users";
    }

}
