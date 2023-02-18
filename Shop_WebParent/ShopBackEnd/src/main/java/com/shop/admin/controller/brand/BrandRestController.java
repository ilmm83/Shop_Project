package com.shop.admin.controller.brand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.admin.exception.brand.BrandNotFoundException;
import com.shop.admin.exception.brand.BrandNotFoundRestException;
import com.shop.admin.service.brand.BrandService;
import com.shop.dto.CategoryDTO;
import com.shop.model.Category;

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

  @GetMapping("/{id}/categories")
  public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id") Long brandId) throws BrandNotFoundRestException {
    if (brandId == 0) return Collections.emptyList();
    var listCategories = new ArrayList<CategoryDTO>();
    try {
      var brand = service.findById(brandId);
      brand.getCategories().forEach(cat -> listCategories.add(convertToCategoryDTO(cat)));
    } catch (BrandNotFoundException e) {
      e.printStackTrace();
      throw new BrandNotFoundRestException();
    }
    return listCategories;
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
