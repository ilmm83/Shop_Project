package com.shop.site.prouduct;

import com.common.model.Category;
import com.common.model.Product;
import com.shop.site.category.CategoryNotFoundException;
import com.shop.site.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;


    @GetMapping("/c/{category_alias}")
    public String viewFirstPage(@PathVariable("category_alias") String categoryAlias, Model model) {
        try {
            var category = categoryService.getCategoryByAlias(categoryAlias);
            var page = productService.listByCategory(0, category.getId());

            changingDisplayProductsPage(1, model, page, category);
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            return "error/404";
        }

        return "products_by_category";
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetails(@PathVariable("product_alias") String productAlias, Model model) {
        try {
            var product = productService.getProductByAlias(productAlias);
            var parents = categoryService.getCategoryParents(product.getCategory());

            model.addAttribute("parents", parents);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getName());

            return "product_details";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/c/{category_alias}/{page_num}")
    public String viewCategoryByPage(@PathVariable("category_alias") String categoryAlias, @PathVariable("page_num") int pageNum, Model model) {
        try {
            var category = categoryService.getCategoryByAlias(categoryAlias);
            var page = productService.listByCategory(pageNum - 1, category.getId());

            changingDisplayProductsPage(pageNum, model, page, category);
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            return "error/404";
        }

        return "products_by_category";
    }

    @GetMapping("/p/search")
    public String viewProductsByKeyword(@RequestParam("keyword") String keyword, Model model) {
        var page = productService.searchByKeyword(keyword, 1);

        model.addAttribute("keyword", keyword);
        model.addAttribute("products", page.getContent());
        model.addAttribute("currentPage", 1);
        model.addAttribute("lastPage", (page.getTotalElements() / ProductService.SEARCH_PER_PAGE) + 1);
        model.addAttribute("totalPages", page.getTotalElements());
        model.addAttribute("pageTitle", keyword + " - Search Result");

        return "search_result";
    }

    @GetMapping("/p/search/{page_num}")
    public String viewProductsByKeywordPageable(@Param("keyword") String keyword, @PathVariable("page_num") int pageNum, Model model) {
        var page = productService.searchByKeyword(keyword, pageNum);

        model.addAttribute("keyword", keyword);
        model.addAttribute("products", page.getContent());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("lastPage", (page.getTotalElements() / ProductService.SEARCH_PER_PAGE) + 1);
        model.addAttribute("totalPages", page.getTotalElements());
        model.addAttribute("pageTitle", keyword + " - Search Result");

        return "search_result";
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
