package com.shop.admin.controller.product;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.admin.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductRestController {

  private final ProductService service;

  @PostMapping("/check_name")
  public String checkNameUnique(@Param("id") Long id, @Param("name") String name) {
    return service.checkNameUnique(id, name);
  }
}
