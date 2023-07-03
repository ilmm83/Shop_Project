package com.shop.admin.controller;

import com.shop.admin.category.CategoryRestController;
import com.shop.admin.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
public class CategoryRestControllerTest {

    @Mock
    CategoryService service;

    CategoryRestController controller;


    @BeforeEach
    void setup() {
        controller = new CategoryRestController(service);
    }

    @Test
    void canCheckNameAndAliasUnique() {
        // given
        var expectedResult = "OK";

        willReturn(expectedResult).given(service).checkNameAndAliasUnique(1L, "name", "alias");

        // when
        var result = controller.checkNameAndAliasUnique(1L, "name", "alias");

        // then
        assertEquals(expectedResult, result);
    }
}
