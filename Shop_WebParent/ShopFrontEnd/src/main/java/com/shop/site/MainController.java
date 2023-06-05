package com.shop.site;

import com.shop.site.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;


    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("categories", categoryService.listNoChildrenCategories());
        return "index";
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) return "customers/login";
        return "redirect:/";
    }
}
