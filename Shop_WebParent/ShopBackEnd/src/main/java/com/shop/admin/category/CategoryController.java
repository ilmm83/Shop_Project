package com.shop.admin.category;

import java.io.IOException;

import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.paging.PagingAndSortingParam;
import com.shop.admin.utils.FileNotSavedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.utils.FileUploadUtil;
import com.shop.admin.utils.exporter.category.CategoryCSVExporter;
import com.shop.dto.CategoryDTO;
import com.shop.model.Category;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private static final String REDIRECT_API_V1_CATEGORIES = "redirect:/api/v1/categories";
    private final CategoryService service;

    @GetMapping
    public String viewCategories() {
        return "redirect:/api/v1/categories/1?sortField=id&sortDir=asc";
    }

    @GetMapping("/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "categories", moduleURL = "/api/v1/categories") PagingAndSortingHelper helper,
                             @PathVariable int pageNum) {
        service.findAllCategoriesSortedBy(pageNum, helper);
        return "categories/categories";
    }

    @GetMapping("/new")
    public String viewCategoryForm(Model model) {

        model.addAttribute("categories", service.listCategoriesHierarchical());
        model.addAttribute("categoryDTO", new CategoryDTO());
        model.addAttribute("category", new Category());
        model.addAttribute("moduleURL", "/api/v1/categories");
        return "categories/categories_form";
    }

    @PostMapping("/new")
    public String createNewCategory(@RequestParam("fileImage") MultipartFile multipart, CategoryDTO dto, RedirectAttributes attributes) {
        service.createNewCategory(multipart, convertToCategory(dto));

        attributes.addFlashAttribute("message", "The category has been saved successfully!");
        return REDIRECT_API_V1_CATEGORIES;
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") long id, RedirectAttributes attributes) {
        try {
            service.delete(id);
            attributes.addFlashAttribute("message", "The category with ID: " + id + " has been deleted successfully!");
        } catch (CategoryNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }
        return REDIRECT_API_V1_CATEGORIES;
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        try {
            var category = service.findById(id);

            model.addAttribute("categories", service.listCategoriesHierarchical());
            model.addAttribute("categoryDTO", convertToCategoryDTO(category));
            model.addAttribute("category", category);
            model.addAttribute("moduleURL", "/api/v1/categories");

        } catch (CategoryNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }

        return "categories/categories_form";
    }

    @GetMapping("/enabled/true/{id}")
    public String changeEnableStateToEnabled(@PathVariable("id") Long id, RedirectAttributes redirect) {
        redirect.addFlashAttribute("message", "Category with ID: " + id + " is now Enabled.");
        service.changeEnableState(id, true);
        return REDIRECT_API_V1_CATEGORIES;
    }

    @GetMapping("/enabled/false/{id}")
    public String changeEnableStateToDisabled(@PathVariable("id") Long id, RedirectAttributes redirect) {
        redirect.addFlashAttribute("message", "Category with ID: " + id + " is now Disabled.");
        service.changeEnableState(id, false);
        return REDIRECT_API_V1_CATEGORIES;
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        var categories = service.findAllCategoriesSortedByName();
        var exporter = new CategoryCSVExporter();
        exporter.export(categories, response);
    }

    private Category convertToCategory(CategoryDTO dto) {
        var category = new Category();
        category.setId(dto.getId());
        category.setParent(dto.getParentId() == 0 ? null : new Category(dto.getParentId()));
        category.setName(dto.getName());
        category.setAlias(dto.getAlias());
        category.setImage(dto.getImage());
        category.setEnabled(dto.isEnabled());
        category.setAllParentIDs(dto.getAllParentIDs());

        return category;
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        var dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setParentId(category.getParent() == null ? 0 : category.getParent().getId());
        dto.setName(category.getName());
        dto.setAlias(category.getAlias());
        dto.setImage(category.getImage());
        dto.setEnabled(category.isEnabled());
        dto.setAllParentIDs(category.getAllParentIDs());

        return dto;
    }
}