package com.shop.site.controller.prouduct;

import com.shop.model.Category;
import com.shop.model.Product;
import com.shop.site.exception.category.CategoryNotFoundException;
import com.shop.site.service.category.CategoryService;
import com.shop.site.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/c")
@RequiredArgsConstructor
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/{category_alias}")
    public String viewFirstPage(@PathVariable("category_alias") String categoryAlias, Model model) {
        return viewCategoryByPage(categoryAlias, 1, model);
    }


    @GetMapping("/{category_alias}/{page_num}")
    public String viewCategoryByPage(@PathVariable("category_alias") String categoryAlias, @PathVariable("page_num") int pageNum, Model model) {
        try {
            var category = categoryService.getCategoryByAlias(categoryAlias);
            if (category == null) return "error/404";

            var page = productService.listByCategory(pageNum - 1, category.getId());
            changingDisplayProductsPage(pageNum, model, page, category);

        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            return "error/404";
        }

        return "products_by_category";
    }

    private void changingDisplayProductsPage(int pageNum, Model model, Page<Product> page, Category category) {

        model.addAttribute("products", page.getContent());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("lastPage", (page.getTotalElements() / ProductService.PRODUCTS_PER_PAGE) + 1);
        model.addAttribute("totalPages", page.getTotalElements());
        model.addAttribute("parents", categoryService.getCategoryParents(category));
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("category", category);


    }
}
