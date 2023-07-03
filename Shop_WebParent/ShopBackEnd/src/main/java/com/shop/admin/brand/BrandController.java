package com.shop.admin.brand;

import com.common.dto.BrandDTO;
import com.common.model.Brand;
import com.common.model.Category;
import com.shop.admin.category.CategoryService;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.paging.PagingAndSortingParam;
import com.shop.admin.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final BrandService brandService;
    private final CategoryService categoryService;

    private static final String REDIRECT_API_V1_BRANDS = "redirect:/api/v1/brands";


    @GetMapping
    public String viewFirstPage() {
        return "redirect:/api/v1/brands/1?sortField=id&sortDir=asc";
    }

    @GetMapping("/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "brands", moduleURL = "/api/v1/brands") PagingAndSortingHelper helper,
                             @PathVariable int pageNum) {

        brandService.findAllBrandsSortedBy(pageNum, helper);

        return "brands/brands";
    }

    @GetMapping("/new")
    public String showBrandCreationPage(Model model) {
        model.addAttribute("categories", categoryService.listCategoriesHierarchical());
        model.addAttribute("brandDTO", new BrandDTO());
        model.addAttribute("moduleURL", "/api/v1/brands");

        return "brands/brands_form";
    }

    @PostMapping("/new")
    public String createNewBrand(@RequestParam("fileImage") MultipartFile multipart, BrandDTO dto, RedirectAttributes attributes) {
        brandService.createNewBrand(convertToBrand(dto), multipart);

        attributes.addFlashAttribute("message", "The brand has been saved successfully!");

        return REDIRECT_API_V1_BRANDS;
    }

    @GetMapping("/edit/{id}")
    public String viewEditPage(@PathVariable("id") Long id, Model model) {
        try {
            var brand = brandService.findById(id);

            model.addAttribute("brandDTO", convertToBrandDTO(brand));
            model.addAttribute("categories", categoryService.listCategoriesHierarchical());
            model.addAttribute("moduleURL", "/api/v1/brands");

        } catch (BrandNotFoundException e) {
            e.printStackTrace();
        }

        return "brands/brands_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            brandService.deleteById(id);

            var uploadDir = "./Shop_WebParent/brands-images/" + id;
            FileUploadUtil.folderCleaner(Path.of(uploadDir));

            attributes.addFlashAttribute("message", "The brand with ID: " + id + " has been deleted successfully!");

        } catch (BrandNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }

        return REDIRECT_API_V1_BRANDS;
    }

    private Brand convertToBrand(BrandDTO dto) {
        var brand = new Brand();
        var categories = new HashSet<Category>();

        for (var id : dto.getParentIds()) {
            if (id == 0) continue;

            var category = categoryService.findById(id);
            if (category != null) categories.add(category);
        }

        brand.setId(dto.getId());
        brand.setCategories(dto.getParentIds() == null ? Collections.emptySet() : categories);
        brand.setName(dto.getName());
        brand.setLogo(dto.getLogo());

        return brand;
    }

    private BrandDTO convertToBrandDTO(Brand brand) {
        var dto = new BrandDTO();
        var categoriesIds = new LinkedList<Long>();
        brand.getCategories().forEach(cat -> categoriesIds.add(cat.getId()));

        dto.setId(brand.getId());
        dto.setParentIds(brand.getCategories() == null ? Collections.emptyList() : categoriesIds);
        dto.setName(brand.getName());
        dto.setLogo(brand.getLogo());

        return dto;
    }
}
