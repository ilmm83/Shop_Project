package com.shop.admin.brand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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

import com.shop.admin.category.CategoryNotFoundException;
import com.shop.admin.category.CategoryService;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.dto.BrandDTO;
import com.shop.model.Brand;
import com.shop.model.Category;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final CategoryService catService;
    private static final String REDIRECT_API_V1_BRANDS = "redirect:/api/v1/brands";

    @GetMapping
    public String brandsPage(Model model) {
        Page<Brand> page = brandService.findAllBrandsSortedBy(null, 1, "name", "asc");
        changingDisplayBrandsPage(1, model, page, "name", "asc", null);

        return "brands/brands";
    }

    @GetMapping("/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum, @Param("sortField") String sortField,
            @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {

        Page<Brand> page = brandService.findAllBrandsSortedBy(keyword, pageNum, sortField, sortDir);
        changingDisplayBrandsPage(pageNum, model, page, sortField, sortDir, keyword);
        return "brands/brands";
    }

    @GetMapping("/new")
    public String newBrandCreationPage(Model model) {
        model.addAttribute("categories", catService.listCategoriesHierarchal());
        model.addAttribute("brandDTO", new BrandDTO());

        return "brands/brands_form";
    }

    @PostMapping("/new")
    public String createNewBrand(BrandDTO dto, @RequestParam("fileImage") MultipartFile multipart, Model model,
            RedirectAttributes attributes) throws IOException, CategoryNotFoundException, BrandNotFoundException {

        var brand = convertToBrand(dto);
        if (!multipart.isEmpty()) {
            var fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
            brand.setLogo(fileName);

            var saved = brandService.save(brand)
                     .orElseThrow(() -> new BrandNotFoundException("Brand was not saved."));

            var uploadDir = "E:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\brands-images\\" + saved.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipart);
        } else
            brandService.save(brand);

        attributes.addFlashAttribute("message", "The brand has been saved successfully!");
        return REDIRECT_API_V1_BRANDS;
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") Long id, Model model, RedirectAttributes attributes)
            throws BrandNotFoundException {
        try {
            var brand = brandService.findById(id);

            model.addAttribute("brandDTO", convertToBrandDTO(brand));
            model.addAttribute("categories", catService.listCategoriesHierarchal());
        } catch (BrandNotFoundException e) {
            e.printStackTrace();
            throw new BrandNotFoundException(e.getMessage());
        }

        return "brands/brands_form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            brandService.deleteById(id);
            var uploadDir = "E:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\brands-images\\" + id;
            FileUploadUtil.folderCleaner(Path.of(uploadDir));
            attributes.addFlashAttribute("message", "The brand with ID: " + id + " has been deleted successfully!");
        } catch (BrandNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }
        return REDIRECT_API_V1_BRANDS;
    }

    private void changingDisplayBrandsPage(int pageNum, Model model, Page<Brand> page, String sortField, String sortDir,
            String keyword) {

        String reverseSortOrder = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("keyword", keyword);
        model.addAttribute("brands", page.getContent());
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("reverseSortOrder", reverseSortOrder);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("lastPage", (page.getTotalElements() / BrandService.PAGE_SIZE) + 1);
        model.addAttribute("totalPages", page.getTotalElements());
    }

    private Brand convertToBrand(BrandDTO dto) throws CategoryNotFoundException {
        var brand = new Brand();
        var categories = new HashSet<Category>();
        for (var id : dto.getParentIds()) {
            if (id == 0)
                continue;
            var cat = catService.findById(id);
            if (cat != null)
                categories.add(cat);
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
