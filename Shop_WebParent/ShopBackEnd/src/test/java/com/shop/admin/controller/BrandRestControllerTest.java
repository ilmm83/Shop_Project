package com.shop.admin.controller;


import com.common.model.Brand;
import com.shop.admin.brand.BrandRestController;
import com.shop.admin.brand.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BrandRestControllerTest {

    @Mock
    BrandService service;

    BrandRestController controller;


    @BeforeEach
    void setup() {
        controller = new BrandRestController(service);
    }

    @Test
    void canCheckNameUnique() {
        // given
        var expectedResult = "OK";
        var brandId = 1L;

        given(service.checkNameUnique(brandId, "name")).willReturn(expectedResult);

        // when
        var result = controller.checkNameUnique(brandId, "name");

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void canListCategoriesByBrand() {
        // given
        var brandId = 1L;
        var expectedBrand = new Brand();
        expectedBrand.setId(brandId);

        given(service.findById(brandId)).willReturn(expectedBrand);

        // when
        controller.listCategoriesByBrand(brandId);

        // then
        verify(service, times(1)).findById(brandId);
    }
}
