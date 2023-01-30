package com.shop.admin.controller.category;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
  public String createNewCategory(Category category, @RequestParam("fileImage") MultipartFile multipart, Model model) throws IOException {
    String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
    category.setImage(fileName);
    
    var saved = service.save(category);
    String uploadDir = "../categories-images/" + saved.getId();
    FileUploadUtil.saveFile(uploadDir, fileName, multipart);
    
    return "redirect:/api/v1/categories";
  }
}
