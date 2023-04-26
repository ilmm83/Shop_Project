package com.shop.site;

import com.shop.site.category.CategoryService;
import com.shop.site.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;
    private final CustomerService customerService;


    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("categories", categoryService.listNoChildrenCategories());
        return "index";
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model) {
        return customerService.checkVerificationCode(code) ? "customers/verify_success" : "customers/verify_fail";
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) return "customers/login";
        return "redirect:/";
    }
}
