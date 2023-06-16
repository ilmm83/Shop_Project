package com.shop.site.controller;

import com.common.model.Category;
import com.common.model.Product;
import com.shop.site.category.CategoryNotFoundException;
import com.shop.site.category.CategoryService;
import com.shop.site.prouduct.ProductController;
import com.shop.site.prouduct.ProductNotFoundException;
import com.shop.site.prouduct.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ProductControllerTest {

    @Mock
    CategoryService categoryService;

    @Mock
    ProductService productService;

    @Mock
    Model model;

    ProductController controller;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductController(categoryService, productService);
    }

    @Test
    void viewFirstPage_Success() {
        // given
        var expectedViewName = "products_by_category";
        var expectedCategoryAlias = "electronics";
        var expectedPageContent = List.of(new Product());
        var page = new PageImpl<>(expectedPageContent);
        var expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setName("Electronics");

        given(categoryService.getCategoryByAlias(expectedCategoryAlias)).willReturn(expectedCategory);
        given(productService.listByCategory(0, expectedCategory.getId())).willReturn(page);

        // when
        var viewName = controller.viewFirstPage(expectedCategoryAlias, model);

        // then
        verify(categoryService, times(1)).getCategoryByAlias(expectedCategoryAlias);
        assertEquals(expectedViewName, viewName);
    }

    @Test
    void viewFirstPage_Fail() {
        // given
        var expectedViewName = "error/404";
        var expectedCategoryAlias = "electronics";
        var expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setName("Electronics");

        given(categoryService.getCategoryByAlias(expectedCategoryAlias)).willReturn(expectedCategory);
        given(productService.listByCategory(0, expectedCategory.getId())).willThrow(CategoryNotFoundException.class);

        // when
        var viewName = controller.viewFirstPage(expectedCategoryAlias, model);

        // then
        verify(categoryService, times(1)).getCategoryByAlias(expectedCategoryAlias);
        assertEquals(expectedCategory, categoryService.getCategoryByAlias(expectedCategoryAlias));
        assertEquals(expectedViewName, viewName);
    }

    @Test
    void viewProductDetails_Success() {
        // given
        var expectedProductAlias = "test-product-alias";
        var expectedViewName = "product_details";
        var expectedCategoryParents = List.of(new Category());
        var expectedProduct = new Product();
        expectedProduct.setCategory(new Category());

        given(productService.getProductByAlias(expectedProductAlias)).willReturn(expectedProduct);
        given(categoryService.getCategoryParents(expectedProduct.getCategory())).willReturn(expectedCategoryParents);
        given(model.getAttribute("parents")).willReturn(expectedCategoryParents);
        given(model.getAttribute("product")).willReturn(expectedProduct);
        given(model.getAttribute("pageTitle")).willReturn(expectedProduct.getName());

        // when
        var viewName = controller.viewProductDetails(expectedProductAlias, model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedProduct, model.getAttribute("product"));
        assertEquals(expectedCategoryParents, model.getAttribute("parents"));
        assertEquals(expectedProduct.getName(), model.getAttribute("pageTitle"));
    }

    @Test
    void viewProductDetails_Fail() {
        // given
        var expectedProductAlias = "test-product-alias";
        var expectedViewName = "error/404";

        given(productService.getProductByAlias(expectedProductAlias)).willThrow(ProductNotFoundException.class);

        // when
        var viewName = controller.viewProductDetails(expectedProductAlias, model);

        // then
        assertEquals(expectedViewName, viewName);
    }

    @Test
    void viewCategoryByPage_Success() {
        // given
        var expectedViewName = "products_by_category";
        var expectedCategoryParents = List.of(new Category());
        var expectedPageNum = 1;
        var expectedLastPage = 2;
        var categoryAlias = "Electronics";
        var page = new PageImpl<>(List.of(new Product()));
        var expectedTotalElements = page.getTotalElements();
        var expectedCategory = new Category();
        expectedCategory.setId(1L);
        expectedCategory.setName("Electronics");

        given(categoryService.getCategoryByAlias(categoryAlias)).willReturn(expectedCategory);
        given(categoryService.getCategoryParents(expectedCategory)).willReturn(expectedCategoryParents);
        given(productService.listByCategory(0, expectedCategory.getId())).willReturn(page);
        given(model.getAttribute("products")).willReturn(page.getContent());
        given(model.getAttribute("currentPage")).willReturn(expectedPageNum);
        given(model.getAttribute("lastPage")).willReturn(expectedLastPage);
        given(model.getAttribute("totalPages")).willReturn(expectedTotalElements);
        given(model.getAttribute("parents")).willReturn(expectedCategoryParents);
        given(model.getAttribute("category")).willReturn(expectedCategory);

        // when
        var viewName = controller.viewCategoryByPage(categoryAlias, expectedPageNum, model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedCategoryParents, model.getAttribute("parents"));
        assertEquals(expectedPageNum, model.getAttribute("currentPage"));
        assertEquals(expectedLastPage, model.getAttribute("lastPage"));
        assertEquals(expectedCategory, model.getAttribute("category"));
        assertEquals(expectedTotalElements, model.getAttribute("totalPages"));
    }

    @Test
    void viewCategoryByPage_Fail() {
        // given
        var expectedViewName = "error/404";
        var categoryAlias = "Electronics";

        given(categoryService.getCategoryByAlias(categoryAlias)).willThrow(CategoryNotFoundException.class);

        // when
        var viewName = controller.viewCategoryByPage(categoryAlias, 1, model);

        // then
        assertEquals(expectedViewName, viewName);
    }

    @Test
    void viewProductsByKeyword() {
        // given
        var expectedPageContent = List.of(new Product());
        var page = new PageImpl<>(expectedPageContent);
        var keyword = "keyword";
        var expectedPageNum = 1;
        var expectedLastPage = 3;
        var expectedTotalPages = page.getTotalElements();
        var expectedPageTitle = keyword + " - Search Result";
        var expectedViewName = "search_result";

        given(productService.searchByKeyword(keyword, expectedPageNum)).willReturn(page);
        given(model.getAttribute("keyword")).willReturn(keyword);
        given(model.getAttribute("products")).willReturn(expectedPageContent);
        given(model.getAttribute("currentPage")).willReturn(expectedPageNum);
        given(model.getAttribute("lastPage")).willReturn(expectedLastPage);
        given(model.getAttribute("totalPages")).willReturn(expectedTotalPages);
        given(model.getAttribute("pageTitle")).willReturn(expectedPageTitle);

        // when
        var viewName = controller.viewProductsByKeyword(keyword, model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedPageContent, model.getAttribute("products"));
        assertEquals(expectedPageNum, model.getAttribute("currentPage"));
        assertEquals(expectedLastPage, model.getAttribute("lastPage"));
        assertEquals(expectedTotalPages, model.getAttribute("totalPages"));
        assertEquals(expectedPageTitle, model.getAttribute("pageTitle"));
    }

    @Test
    void viewProductsByKeywordPageable() {
        // given
        var expectedPageContent = List.of(new Product());
        var page = new PageImpl<>(expectedPageContent);
        var keyword = "keyword";
        var expectedPageNum = 1;
        var expectedLastPage = 3;
        var expectedTotalPages = page.getTotalElements();
        var expectedPageTitle = keyword + " - Search Result";
        var expectedViewName = "search_result";

        given(productService.searchByKeyword(keyword, expectedPageNum)).willReturn(page);
        given(model.getAttribute("keyword")).willReturn(keyword);
        given(model.getAttribute("products")).willReturn(expectedPageContent);
        given(model.getAttribute("currentPage")).willReturn(expectedPageNum);
        given(model.getAttribute("lastPage")).willReturn(expectedLastPage);
        given(model.getAttribute("totalPages")).willReturn(expectedTotalPages);
        given(model.getAttribute("pageTitle")).willReturn(expectedPageTitle);

        // when
        var viewName = controller.viewProductsByKeywordPageable(keyword, expectedPageNum, model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedPageContent, model.getAttribute("products"));
        assertEquals(expectedPageNum, model.getAttribute("currentPage"));
        assertEquals(expectedLastPage, model.getAttribute("lastPage"));
        assertEquals(expectedTotalPages, model.getAttribute("totalPages"));
        assertEquals(expectedPageTitle, model.getAttribute("pageTitle"));
    }
}