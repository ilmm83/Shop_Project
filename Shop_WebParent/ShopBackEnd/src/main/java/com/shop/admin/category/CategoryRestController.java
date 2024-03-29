package com.shop.admin.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryRestController {

    private final CategoryService service;


    @PostMapping("/check_name_and_alias")
    public String checkNameAndAliasUnique(@Param("id") Long id, @Param("name") String name, @Param("alias") String alias) {
        return service.checkNameAndAliasUnique(id, name, alias);
    }
}
