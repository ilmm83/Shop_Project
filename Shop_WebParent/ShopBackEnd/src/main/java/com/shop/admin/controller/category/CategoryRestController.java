package com.shop.admin.controller.category;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.admin.service.category.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryRestController {

  private final CategoryService service;

  @PostMapping("/check_name_and_alias")
  public String checkNameAndAliasUnique(@Param("id") Long id, @Param("name") String name, @Param("alias") String alias) {
    return service.checkNameAndAliasUnique(id, name, alias);
  }
}
