package controller;

import com.common.dto.CategoryDTO;
import com.common.model.Category;
import com.shop.admin.category.CategoryController;
import com.shop.admin.category.CategoryNotFoundException;
import com.shop.admin.category.CategoryService;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.utils.exporter.CategoryCSVExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    static final String REDIRECT_API_V1_CATEGORIES = "redirect:/api/v1/categories";

    @Mock
    CategoryService service;

    @Mock
    Model model;

    @Mock
    RedirectAttributes attributes;

    CategoryController controller;


    @BeforeEach
    void setup() {
        controller = new CategoryController(service);
    }

    @Test
    void canViewCategories() {
        // given
        var expectedResult = "redirect:/api/v1/categories/1?sortField=id&sortDir=asc";

        // when
        var result = controller.viewCategories();

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void canListByPage() {
        // given
        var expectedViewName = "categories/categories";
        var helper = mock(PagingAndSortingHelper.class);

        willDoNothing().given(service).findAllCategoriesSortedBy(1, helper);

        // when
        var viewName = controller.listByPage(helper, 1);

        // then
        assertEquals(expectedViewName, viewName);
        verify(service, times(1)).findAllCategoriesSortedBy(1, helper);
    }

    @Test
    void canViewCategoryForm() {
        // given
        var expectedViewName = "categories/categories_form";
        var expectedModuleName = "/api/v1/categories";
        var expectedCategories = List.of(new Category());
        var expectedCategoryDTO = new CategoryDTO();
        var expectedCategory = new Category();
        var model = mock(Model.class);

        given(service.listCategoriesHierarchical()).willReturn(expectedCategories);
        given(model.getAttribute("categories")).willReturn(expectedCategories);
        given(model.getAttribute("categoryDTO")).willReturn(expectedCategoryDTO);
        given(model.getAttribute("category")).willReturn(expectedCategory);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleName);

        // when
        var viewName = controller.viewCategoryForm(model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedCategories, model.getAttribute("categories"));
        assertEquals(expectedCategoryDTO, model.getAttribute("categoryDTO"));
        assertEquals(expectedCategory, model.getAttribute("category"));
        assertEquals(expectedModuleName, model.getAttribute("moduleURL"));
    }

    @Test
    void canCreateNewCategory() {
        // Given
        var categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Test");
        categoryDTO.setParentId(0L);
        categoryDTO.setAlias("TestAlias");
        categoryDTO.setImage(null);
        categoryDTO.setEnabled(true);
        categoryDTO.setAllParentIDs(null);

        var multipart = new MockMultipartFile("fileImage", "hello.png", "image/png", "Hello, World!".getBytes());

        var attributes = new RedirectAttributesModelMap();

        // When
        var result = controller.createNewCategory(multipart, categoryDTO, attributes);

        // Then
        var categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(service, times(1)).createNewCategory(any(MultipartFile.class), categoryArgumentCaptor.capture());

        var capturedCategory = categoryArgumentCaptor.getValue();
        assertEquals(categoryDTO.getId(), capturedCategory.getId());
        assertEquals(categoryDTO.getName(), capturedCategory.getName());
        assertEquals(categoryDTO.getAlias(), capturedCategory.getAlias());
        assertEquals(categoryDTO.isEnabled(), capturedCategory.isEnabled());

        assertEquals("redirect:/api/v1/categories", result);
    }

    @Test
    void canDeleteCategory_Success() {
        // given
        var expectedMessage = "The category with ID: 1 has been deleted successfully!";

        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(service).delete(1L);

        // when
        var result = controller.deleteCategory(1L, attributes);

        // then
        assertEquals(REDIRECT_API_V1_CATEGORIES, result);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        verify(service, times(1)).delete(1L);
    }

    @Test
    void canDeleteCategory_Fail() {
        // Given
        long categoryId = 1L;
        var exceptionMessage = "Could not find any category with ID " + categoryId;

        // Define the behavior of service method
        willThrow(new CategoryNotFoundException(exceptionMessage))
            .given(service).delete(categoryId);

        // When
        var result = controller.deleteCategory(categoryId, attributes);

        // Then
        assertThrows(CategoryNotFoundException.class, () -> service.delete(categoryId));
        verify(attributes).addFlashAttribute(eq("message"), eq(exceptionMessage));
        assertEquals(REDIRECT_API_V1_CATEGORIES, result);
    }

    @Test
    void canEditPage_Success() {
        // given
        var expectedCategories = List.of(new Category());
        var expectedViewName = "categories/categories_form";
        var categoryId = 1L;
        var expectedCategory = new Category();
        expectedCategory.setId(categoryId);

        given(service.findById(categoryId)).willReturn(expectedCategory);
        given(service.listCategoriesHierarchical()).willReturn(expectedCategories);

        // when
        var viewName = controller.viewEditPage(categoryId, model, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedCategory, service.findById(categoryId));
        assertEquals(expectedCategories, service.listCategoriesHierarchical());
    }

    @Test
    void canEditPage_Fail() {
        // given
        willThrow(CategoryNotFoundException.class).given(service).findById(1L);

        // when
        controller.viewEditPage(1L, model, mock(RedirectAttributes.class));

        // then
        assertThrows(CategoryNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void canChangeEnableStateToEnabled() {
        // given
        var expectedMessage = "Category with ID: 1 is now Enabled.";

        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(service).changeEnableState(1L, true);

        // when
        var viewName = controller.changeEnableStateToEnabled(1L, attributes);

        // then
        assertEquals(REDIRECT_API_V1_CATEGORIES, viewName);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        verify(service, times(1)).changeEnableState(1L, true);
    }

    @Test
    void canChangeEnableStateToDisabled() {
        // given
        var expectedMessage = "Category with ID: 1 is now Disabled.";

        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(service).changeEnableState(1L, false);

        // when
        var viewName = controller.changeEnableStateToDisabled(1L, attributes);

        // then
        assertEquals(REDIRECT_API_V1_CATEGORIES, viewName);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        verify(service, times(1)).changeEnableState(1L, false);
    }

    @Test
    void canExportToCSV() throws IOException {
        // given
        var category = new Category();
        category.setId(1L);
        category.setParent(new Category());
        category.setAlias("alias");
        category.setName("name");

        var expectedCategories = List.of(category);
        var exporter = mock(CategoryCSVExporter.class);
        var response = mock(HttpServletResponse.class);
        var writer = mock(PrintWriter.class);

        willReturn(expectedCategories).given(service).listCategoriesHierarchical();
        willReturn(writer).given(response).getWriter();

        // when
        controller.exportToCSV(response);
        exporter.export(expectedCategories, response);

        // then
        verify(exporter, times(1)).export(expectedCategories, response);
        assertEquals(expectedCategories, service.listCategoriesHierarchical());
    }
}
