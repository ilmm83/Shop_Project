package com.shop.admin.controller.brand;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.admin.service.brand.BrandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/brands")
@RequiredArgsConstructor
public class BrandRestController {

  private final BrandService service;

  @PostMapping("/check_name")
  public String checkNameUnique(@Param("id") Long id, @Param("name") String name) {
    return service.checkNameUnique(id, name);
  }
}
