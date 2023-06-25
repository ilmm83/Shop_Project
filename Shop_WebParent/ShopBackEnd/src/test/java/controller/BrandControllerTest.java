package controller;

import com.common.dto.BrandDTO;
import com.common.model.Brand;
import com.common.model.Category;
import com.shop.admin.brand.BrandController;
import com.shop.admin.brand.BrandNotFoundException;
import com.shop.admin.brand.BrandService;
import com.shop.admin.category.CategoryService;
import com.shop.admin.paging.PagingAndSortingHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandControllerTest {

    @Mock
    BrandService brandService;

    @Mock
    CategoryService categoryService;

    @Mock
    PagingAndSortingHelper helper;

    @Mock
    Model model;

    BrandController controller;


    @BeforeEach
    void setup() {
        controller = new BrandController(brandService, categoryService);
    }

    @Test
    void canViewFirstPage() {
        // given
        var expectedResult = "redirect:/api/v1/brands/1?sortField=id&sortDir=asc";

        // when
        var result = controller.viewFirstPage();

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void canListByPage() {
        // given
        var expectedViewName = "brands/brands";

        willDoNothing().given(brandService).findAllBrandsSortedBy(1, helper);

        // when
        var viewName = controller.listByPage(helper, 1);

        // then
        assertEquals(expectedViewName, viewName);
        verify(brandService, times(1)).findAllBrandsSortedBy(1, helper);
    }

    @Test
    void canShowBrandCreationPage() {
        // given
        var expectedViewName = "brands/brands_form";
        var expectedCategories = List.of(new Category());
        var expectedBrandDTO = new BrandDTO();
        var expectedModuleURL = "/api/v1/brands";

        given(categoryService.listCategoriesHierarchical()).willReturn(expectedCategories);
        given(model.getAttribute("categories")).willReturn(expectedCategories);
        given(model.getAttribute("brandDTO")).willReturn(expectedBrandDTO);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleURL);

        // when
        var viewName = controller.showBrandCreationPage(model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedCategories, model.getAttribute("categories"));
        assertEquals(expectedBrandDTO, model.getAttribute("brandDTO"));
        assertEquals(expectedModuleURL, model.getAttribute("moduleURL"));
    }

    @Test
    void canCreateNewBrand() {
        // given
        var expectedViewName = "redirect:/api/v1/brands";
        var expectedMessage = "The brand has been saved successfully!";
        var multipart = mock(MultipartFile.class);
        var attributes = mock(RedirectAttributes.class);
        var brandDTO = mock(BrandDTO.class);

        given(attributes.getAttribute("message")).willReturn(expectedMessage);

        // when
        var viewName = controller.createNewBrand(multipart, brandDTO, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
    }

    @Test
    void viewEditPage_Success() {
        // given
        var expectedViewName = "brands/brands_form";
        var expectedCategories = List.of(new Category());
        var expectedModuleURL = "/api/v1/brands";
        var expectedBrandDTO = new BrandDTO();
        var expectedBrand = new Brand();
        expectedBrand.setId(1L);

        given(brandService.findById(1L)).willReturn(expectedBrand);
        given(categoryService.listCategoriesHierarchical()).willReturn(expectedCategories);
        given(model.getAttribute("categories")).willReturn(expectedCategories);
        given(model.getAttribute("brandDTO")).willReturn(expectedBrandDTO);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleURL);

        // when
        var viewName = controller.viewEditPage(1L, model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedCategories, model.getAttribute("categories"));
        assertEquals(expectedBrandDTO, model.getAttribute("brandDTO"));
        assertEquals(expectedModuleURL, model.getAttribute("moduleURL"));
        assertEquals(expectedBrand, brandService.findById(1L));
    }

    @Test
    void viewEditPage_Fail() {
        // given
        given(brandService.findById(1L)).willThrow(BrandNotFoundException.class);

        // when
        controller.viewEditPage(1L, model);

        // then
        assertThrows(BrandNotFoundException.class, () -> brandService.findById(1L));
    }

    @Test
    void deleteBrand_Success() {
        // given
        var brandId = 1L;
        var attributes = mock(RedirectAttributes.class);

        given(brandService.deleteById(brandId)).willReturn(any(Brand.class));

        // when
        controller.deleteBrand(brandId, attributes);

        // then
        verify(brandService, times(1)).deleteById(brandId);
    }

    @Test
    void deleteBrand_Fail() {
        // given
        var attributes = mock(RedirectAttributes.class);
        var expectedMessage = "Could not find brand with this ID 1";

        given(brandService.deleteById(1L)).willThrow(BrandNotFoundException.class);
        given(attributes.getAttribute("message")).willReturn(expectedMessage);

        // when
        controller.deleteBrand(1L, attributes);

        // then
        assertThrows(BrandNotFoundException.class, () -> brandService.deleteById(1L));
        assertEquals(expectedMessage, attributes.getAttribute("message"));
    }
}