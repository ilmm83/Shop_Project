package com.shop.admin.controller.category;

import java.io.IOException;

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
import com.shop.admin.exception.user.UserNotFoundException;
import com.shop.admin.service.category.CategoryService;
import com.shop.admin.utils.FileUploadUtil;
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
    model.addAttribute("category", new Category());
    return "categories/categories_form";
  }

  @PostMapping("/new")
  public String createNewCategory(Category category, @RequestParam("fileImage") MultipartFile multipart, Model model,
      RedirectAttributes attributes) throws IOException {

    String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
    category.setImage(fileName);

    var parent = new Category(category.getParentId());
    category.setParent(parent);

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
      attributes.addFlashAttribute("message", "The category with ID: " + id + "has been deleted successfully!");
    } catch (CategoryNotFoundException e) {
      attributes.addFlashAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return "redirect:/api/v1/categories";
  }
}
