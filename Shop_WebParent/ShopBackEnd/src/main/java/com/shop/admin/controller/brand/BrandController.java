package com.shop.admin.controller.brand;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.admin.service.brand.BrandService;
import com.shop.model.Brand;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

  private final BrandService service;

  @GetMapping
  public String brandsPage(Model model) {
    Page<Brand> page = service.findAllCategoriesSortedBy(null, 1, "name", "asc");
    changingDisplayUsersPage(1, model, page, "name", "asc", null);
    
    return "brands/brands";
  } 
  
  private void changingDisplayUsersPage(int pageNum, Model model, Page<Brand> page, String sortField, String sortDir,
      String keyword) {

    String reverseSortOrder = sortDir.equals("asc") ? "desc" : "asc";

    model.addAttribute("keyword", keyword);
    model.addAttribute("brands", page.getContent());
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("sortField", sortField);
    model.addAttribute("reverseSortOrder", reverseSortOrder);
    model.addAttribute("currentPage", pageNum);
    model.addAttribute("lastPage", (page.getTotalElements() / BrandService.PAGE_SIZE) + 1);
    model.addAttribute("totalBrands", page.getTotalElements());
  }
}
