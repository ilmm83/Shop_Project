package com.shop.site;

import com.shop.site.category.CategoryService;
import com.shop.site.customer.CustomerService;
import com.shop.site.security.oauth2.CustomerOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
