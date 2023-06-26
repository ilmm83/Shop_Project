package com.shop.admin.brand;

import com.common.dto.CategoryDTO;
import com.common.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/brands")
public class BrandRestController {

    private final BrandService service;


    @PostMapping("/check_name")
    public String checkNameUnique(@Param("id") Long id, @Param("name") String name) {
        return service.checkNameUnique(id, name);
    }

    @GetMapping("/categories/{id}")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id") Long brandId) {
        if (brandId == 0) {
            return Collections.emptyList();
        }

        var listCategories = new ArrayList<CategoryDTO>();

        try {
            service.findById(brandId)
                .getCategories()
                .forEach(cat -> listCategories.add(convertToCategoryDTO(cat)));

        } catch (BrandNotFoundException e) {
            e.printStackTrace();
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
