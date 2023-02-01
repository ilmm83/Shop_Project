package com.shop.admin.controller.category;

import java.io.IOException;

import org.springframework.security.web.RedirectStrategy;
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

import com.shop.admin.exception.category.CategoryNotFoundException;
import com.shop.admin.service.category.CategoryService;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.dto.CategoryDTO;
import com.shop.model.Category;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService service;

  @GetMapping
  public String categories(Model model) {
    model.addAttribute("categories", service.findAllCategoriesSortedById());
    return "categories/categories";
  }

  @GetMapping("/new")
  public String createForm(Model model) {

    model.addAttribute("categories", service.listCategoriesHierarchal());
    model.addAttribute("categoryDTO", new CategoryDTO());
    model.addAttribute("category", new Category());
    return "categories/categories_form";
  }

  @PostMapping("/new")
  public String createNewCategory(CategoryDTO dto, @RequestParam("fileImage") MultipartFile multipart, Model model,
      RedirectAttributes attributes) throws IOException {

    String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
    var category = convertToCategory(dto);
    category.setImage(fileName);


    var saved = service.save(category);
    String uploadDir = "../categories-images/" + saved.getId();
    FileUploadUtil.saveFile(uploadDir, fileName, multipart);

    attributes.addFlashAttribute("message", "The category has been saved successfully!");
    return "redirect:/api/v1/categories";
  }

  @GetMapping("delete/{id}")
  public String delete(@PathVariable("id") long id, RedirectAttributes attributes) {
    try {
      service.delete(id);
      attributes.addFlashAttribute("message", "The category with ID: " + id + " has been deleted successfully!");
    } catch (CategoryNotFoundException e) {
      attributes.addFlashAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return "redirect:/api/v1/categories";
  }

  @GetMapping("edit/{id}")
  public String editPage(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
    try {
      var category = service.findById(id);
      
      model.addAttribute("categories", service.listCategoriesHierarchal());
      model.addAttribute("categoryDTO", convertToCategoryDTO(category));
      model.addAttribute("category", category);

      attributes.addFlashAttribute("message", "The category with ID: " + id + " has been updated successfully!");
    } catch (CategoryNotFoundException e) {
      attributes.addFlashAttribute("message", e.getMessage());
      e.printStackTrace();
    }

    return "categories/categories_form";
  }

  


  private Category convertToCategory(CategoryDTO dto) {
    var category = new Category();
    category.setId(dto.getId());
    category.setParent(dto.getParentId() == 0 ? null : new Category(dto.getParentId()));
    category.setName(dto.getName());
    category.setAlias(dto.getAlias());
    category.setImage(dto.getImage());
    category.setEnabled(dto.isEnabled());

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

    return dto;
  }
}
