package controller;

import com.common.dto.ProductDTO;
import com.common.model.Brand;
import com.common.model.Category;
import com.common.model.Product;
import com.common.model.ProductImage;
import com.shop.admin.brand.BrandService;
import com.shop.admin.category.CategoryService;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.product.ProductController;
import com.shop.admin.product.ProductNotFoundException;
import com.shop.admin.product.ProductService;
import com.shop.admin.security.ShopUserDetails;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    ProductService productService;

    @Mock
    BrandService brandService;

    @Mock
    CategoryService categoryService;

    @Mock
    Model model;

    @Mock
    RedirectAttributes attributes;

    ProductController controller;


    @BeforeEach
    void setup() {
        controller = new ProductController(productService, brandService, categoryService);
    }

    @Test
    void canViewFirstPage() {
        // given
        var expectedResult = "redirect:/api/v1/products/1?sortField=id&sortDir=asc";

        // when
        var result = controller.viewFirstPage();

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void canViewCreateNewProductPage() {
        // given
        var expectedBrands = List.of(new Brand());
        var expectedCategories = List.of(new Category());
        var expectedProductDTO = new ProductDTO();
        var expectedPageTitle = "Create new Product";
        var expectedModuleURL = "/api/v1/products";
        var expectedResult = "products/products_form";

        given(brandService.findAllByNameAsc()).willReturn(expectedBrands);
        given(categoryService.listCategoriesHierarchical()).willReturn(expectedCategories);
        given(model.getAttribute("brands")).willReturn(expectedBrands);
        given(model.getAttribute("categories")).willReturn(expectedCategories);
        given(model.getAttribute("productDTO")).willReturn(expectedProductDTO);
        given(model.getAttribute("pageTitle")).willReturn(expectedPageTitle);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleURL);

        // when
        var result = controller.viewCreateNewProductPage(model);

        // then
        verify(brandService).findAllByNameAsc();
        verify(categoryService).listCategoriesHierarchical();
        assertEquals(expectedResult, result);
        assertEquals(expectedBrands, model.getAttribute("brands"));
        assertEquals(expectedCategories, model.getAttribute("categories"));
        assertEquals(expectedProductDTO, model.getAttribute("productDTO"));
        assertEquals(expectedPageTitle, model.getAttribute("pageTitle"));
        assertEquals(expectedModuleURL, model.getAttribute("moduleURL"));
    }

    @Test
    void canCreateNewProduct() {
        // Create mock data
        var mainImageMultipart = mock(MultipartFile.class);
        var loggedUser = mock(ShopUserDetails.class);
        var productDTO = mock(ProductDTO.class);
        var extraImagesMultipart = new MultipartFile[0];
        var detailValues = new String[0];
        var detailNames = new String[0];
        var product = new Product();
        product.setId(0L);
        product.setMainImage("main image");

        // Define behavior for the mock services
        given(productService.findById(0L)).willReturn(product);
        given(productService.createNewProduct(any(), any(), any(), any(), any(), any(), any())).willReturn("redirect:/api/v1/products");

        // Call the method with the mock data
        var result = controller.createNewProduct(extraImagesMultipart, mainImageMultipart, detailNames, detailValues, loggedUser, attributes, productDTO);

        // Verify result
        assertEquals("redirect:/api/v1/products", result);
    }

    @Test
    void canViewPageByPageNumber() {
        // given
        var expectedCategories = List.of(new Category(1L));
        var helper = mock(PagingAndSortingHelper.class);

        given(categoryService.listCategoriesHierarchical()).willReturn(expectedCategories);
        given(model.getAttribute("categories")).willReturn(expectedCategories);
        given(model.getAttribute("categoryId")).willReturn(1L);
        willDoNothing().given(productService).findAllProductsSortedBy(1, helper, 1L);

        // when
        var result = controller.viewPageByPageNumber(helper, 1L, 1, model);

        // then
        assertEquals(expectedCategories, model.getAttribute("categories"));
        assertEquals(1L, model.getAttribute("categoryId"));
        assertEquals("products/products", result);
    }

    @Test
    void canViewEditProductPage_Success() {
        // given
        var expectedBrands = List.of(new Brand());
        var expectedCategories = List.of(new Category());
        var expectedPageTitle = "Manage Product with ID: 0";
        var expectedModuleURL = "/api/v1/products";
        var productCategory = new Category();
        var productDTO = new ProductDTO();
        var productBrand = new Brand();
        var product = new Product();
        product.setId(1L);
        product.setCategory(productCategory);
        product.setBrand(productBrand);
        product.setImages(List.of(new ProductImage()));
        product.setMainImage("main image");

        given(productService.findById(1L)).willReturn(product);
        given(brandService.findAllByNameAsc()).willReturn(expectedBrands);
        given(model.getAttribute("brands")).willReturn(expectedBrands);
        given(model.getAttribute("categories")).willReturn(expectedCategories);
        given(model.getAttribute("brand")).willReturn(productBrand);
        given(model.getAttribute("category")).willReturn(productCategory);
        given(model.getAttribute("imagesAmount")).willReturn(1);
        given(model.getAttribute("productDTO")).willReturn(productDTO);
        given(model.getAttribute("pageTitle")).willReturn(expectedPageTitle);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleURL);

        // when
        var result = controller.viewEditProductPage(1L, model, mock(RedirectAttributes.class));

        // then
        assertEquals("products/products_form", result);
        assertEquals(expectedBrands, model.getAttribute("brands"));
        assertEquals(expectedCategories, model.getAttribute("categories"));
        assertEquals(productBrand, model.getAttribute("brand"));
        assertEquals(productCategory, model.getAttribute("category"));
        assertEquals(productDTO, model.getAttribute("productDTO"));
        assertEquals(expectedPageTitle, model.getAttribute("pageTitle"));
        assertEquals(expectedModuleURL, model.getAttribute("moduleURL"));
        assertEquals(1, model.getAttribute("imagesAmount"));
    }

    @Test
    void canViewEditProductPage_Fail() {
        // given
        willThrow(ProductNotFoundException.class).given(productService).findById(1L);

        // when
        controller.viewPageByPageNumber(mock(PagingAndSortingHelper.class), 1L, 1, model);

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void canShowProductDetailsPage_Success() {
        // given
        var productDTO = mock(ProductDTO.class);
        var product = mock(Product.class);
        var productBrand = new Brand();
        var productCategory = new Category();
        var expectedViewName = "products/product_detail_modal";

        given(productService.findById(1L)).willReturn(product);
        given(product.getCategory()).willReturn(productCategory);
        given(product.getBrand()).willReturn(productBrand);
        given(product.getFullDescription()).willReturn("full description");
        given(product.getShortDescription()).willReturn("short description");
        given(product.getMainImage()).willReturn("main image");
        given(model.getAttribute("brand")).willReturn(productBrand);
        given(model.getAttribute("category")).willReturn(productCategory);
        given(model.getAttribute("productDTO")).willReturn(productDTO);
        given(model.getAttribute("imagesAmount")).willReturn(1);

        // when
        var viewName = controller.showProductDetailsPage(1L, model, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(productBrand, model.getAttribute("brand"));
        assertEquals(productCategory, model.getAttribute("category"));
        assertEquals(productDTO, model.getAttribute("productDTO"));
        assertEquals(1, model.getAttribute("imagesAmount"));
    }

    @Test
    void canShowProductDetailsPage_Fail() {
        // given
        var expectedViewName = "redirect:/api/v1/products";
        var expectedErrorMessage = "Could not found product with ID: 1";

        given(productService.findById(1L)).willThrow(ProductNotFoundException.class);
        given(attributes.getAttribute("message")).willReturn(expectedErrorMessage);


        // when
        var viewName = controller.showProductDetailsPage(1L, model, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
        assertEquals(expectedErrorMessage, attributes.getAttribute("message"));
    }

    @Test
    void canChangeTheProductStateToDisableState() {
        // given
        var expectedViewName = "redirect:/api/v1/products";
        var expectedMessage = "The product with ID: 1 is now disabled.";

        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(productService).changeProductState(1L, false);

        // when
        var viewName = controller.changeTheProductStateToDisableState(1L, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        verify(productService).changeProductState(1L, false);
    }

    @Test
    void canChangeTheProductStateToEnableState() {
        // given
        var expectedViewName = "redirect:/api/v1/products";
        var expectedMessage = "The product with ID: 1 is now enabled.";

        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(productService).changeProductState(1L, true);

        // when
        var viewName = controller.changeTheProductStateToEnableState(1L, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        verify(productService).changeProductState(1L, true);
    }

    @Test
    void canDeleteProduct() {
        // given
        var expectedViewName = "redirect:/api/v1/products";

        willDoNothing().given(productService).deleteProduct(1L, attributes);

        // when
        var viewName = controller.deleteProduct(1L, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        verify(productService).deleteProduct(1L, attributes);
    }
}
