package com.shop.admin.product;

import com.common.dto.ProductDTO;
import com.common.model.Product;
import com.shop.admin.brand.BrandService;
import com.shop.admin.category.CategoryService;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.paging.PagingAndSortingParam;
import com.shop.admin.security.ShopUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.shop.admin.product.ProductSaveHelper.isDescriptionsEmpty;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;


    @GetMapping
    public String productPage() {
        return "redirect:/api/v1/products/1?sortField=id&sortDir=asc";
    }

    @GetMapping("/new")
    public String createNewProductPage(Model model) {
        var brands = brandService.findAllByNameAsc();
        var categories = categoryService.listCategoriesHierarchical();
        var dto = ProductDTO.builder()
            .enabled(true)
            .inStock(true)
            .build();

        model.addAttribute("brands", brands);
        model.addAttribute("categories", categories);
        model.addAttribute("productDTO", dto);
        model.addAttribute("pageTitle", "Create new Product");
        model.addAttribute("moduleURL", "/api/v1/products");

        return "products/products_form";
    }

    @PostMapping("/save")
    public String createNewProduct(@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                                   @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImagesMultipart,
                                   @RequestParam(name = "detailName", required = false) String[] detailNames,
                                   @RequestParam(name = "detailValue", required = false) String[] detailValues,
                                   @AuthenticationPrincipal ShopUserDetails loggedUser, RedirectAttributes attributes, ProductDTO dto) {

        return productService.createNewProduct(mainImageMultipart, extraImagesMultipart, detailNames,
            detailValues, loggedUser, attributes, convertToProduct(dto));
    }


    @GetMapping("/{pageNum}")
    public String pageByNumber(@PagingAndSortingParam(listName = "products", moduleURL = "/api/v1/products") PagingAndSortingHelper helper,
                               @PathVariable int pageNum,
                               @RequestParam(name = "categoryId", required = false) Long categoryId,
                               Model model) {

        productService.findAllProductsSortedBy(pageNum, helper, categoryId);

        model.addAttribute("categories", categoryService.listCategoriesHierarchical());

        if (categoryId != null) {
            model.addAttribute("categoryId", categoryId);
        }

        return "products/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        try {
            var product = productService.findById(id);
            var brands = brandService.findAllByNameAsc();
            var categories = categoryService.listCategoriesHierarchical();
            var category = product.getCategory();
            var brand = product.getBrand();

            model.addAttribute("brands", brands);
            model.addAttribute("brand", brand);
            model.addAttribute("categories", categories);
            model.addAttribute("category", category);
            model.addAttribute("productDTO", convertToProductDTO(product));
            model.addAttribute("imagesAmount", product.getImages().size());
            model.addAttribute("pageTitle", "Manage Product with ID: " + id);
            model.addAttribute("moduleURL", "/api/v1/products");

        } catch (ProductNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }

        return "products/products_form";
    }

    @GetMapping("/detail/{id}")
    public String showProductDetails(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        try {
            var product = productService.findById(id);
            var brand = product.getBrand();
            var category = product.getCategory();

            isDescriptionsEmpty(product, "<div><br></div>");

            model.addAttribute("brand", brand);
            model.addAttribute("category", category);
            model.addAttribute("productDTO", convertToProductDTO(product));
            model.addAttribute("imagesAmount", product.getImages().size());

            return "products/product_detail_modal";

        } catch (ProductNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/api/v1/products";
    }

    @GetMapping("/enabled/false/{id}")
    public String changeToDisableState(@PathVariable("id") Long id, RedirectAttributes attributes) {
        productService.changeProductState(id, false);

        attributes.addFlashAttribute("message", "The product with ID: " + id + " is now disabled.");

        return "redirect:/api/v1/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes attributes) {
        productService.deleteProduct(id, attributes);

        return "redirect:/api/v1/products";
    }

    @GetMapping("/enabled/true/{id}")
    public String changeToEnableState(@PathVariable("id") Long id, RedirectAttributes attributes) {
        productService.changeProductState(id, true);
        attributes.addFlashAttribute("message", "The product with ID: " + id + " is now enabled.");

        return "redirect:/api/v1/products";
    }

    private ProductDTO convertToProductDTO(Product product) {
        var dto = new ProductDTO();
        var brandId = product.getBrand() == null ? null : product.getBrand().getId();
        var categoryId = product.getCategory() == null ? null : product.getCategory().getId();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setAlias(product.getAlias());
        dto.setShortDescription(product.getShortDescription());
        dto.setFullDescription(product.getFullDescription());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setEnabled(product.isEnabled());
        dto.setInStock(product.isInStock());
        dto.setPrice(product.getPrice());
        dto.setDiscountPercent(product.getDiscountPercent());
        dto.setCost(product.getCost());
        dto.setLength(product.getLength());
        dto.setWidth(product.getWidth());
        dto.setHeight(product.getHeight());
        dto.setWeight(product.getWeight());
        dto.setMainImage(!product.getMainImage().isEmpty() ? product.getMainImage() : null);
        dto.setDetails(product.getDetails());
        dto.setImages(product.getImages());
        dto.setBrandId(brandId);
        dto.setCategoryId(categoryId);

        return dto;
    }

    private Product convertToProduct(ProductDTO dto) {
        var product = new Product();
        var brand = dto.getBrandId() == 0 ? null : brandService.findById(dto.getBrandId());
        var category = dto.getCategoryId() == 0 ? null : categoryService.findById(dto.getCategoryId());

        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setAlias(dto.getAlias());
        product.setShortDescription(dto.getShortDescription());
        product.setFullDescription(dto.getFullDescription());
        product.setCreatedAt(dto.getCreatedAt());
        product.setUpdatedAt(dto.getUpdatedAt());
        product.setEnabled(dto.isEnabled());
        product.setInStock(dto.isInStock());
        product.setPrice(dto.getPrice());
        product.setDiscountPercent(dto.getDiscountPercent());
        product.setCost(dto.getCost());
        product.setLength(dto.getLength());
        product.setWidth(dto.getWidth());
        product.setHeight(dto.getHeight());
        product.setWeight(dto.getWeight());
        product.setMainImage(
            dto.getMainImage() == null && dto.getId() != null ? productService.findById(dto.getId()).getMainImage()
                : "");
        product.setDetails(dto.getDetails());
        product.setImages(dto.getImages());
        product.setBrand(brand);
        product.setCategory(category);

        return product;
    }
}