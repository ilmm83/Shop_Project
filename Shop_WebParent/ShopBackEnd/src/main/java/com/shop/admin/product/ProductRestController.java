package com.shop.admin.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/products")
public class ProductRestController {

    private final ProductService service;


    @PostMapping("/check_name_and_alias")
    public String checkNameAndAliasUnique(@Param("id") Long id, @Param("name") String name, @Param("alias") String alias) {
        return service.checkNameAndAliasUnique(id, name, alias);
    }

    @PostMapping("/remove_image")
    public void removeImage(@Param("id") Long id, @Param("fileName") String fileName) {
        service.removeImage(id, fileName);
    }
}
