package com.shop.site;

import com.shop.site.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final CategoryService service;

    @GetMapping
    public String viewHomePage(Model model) {
        model.addAttribute("categories", service.listNoChildrenCategories());

        return "index";
    }


}
