package com.shop.admin.product;

import static com.shop.admin.product.ProductSaveHelper.isDescriptionsEmpty;
import static com.shop.admin.product.ProductSaveHelper.setAndSaveMainImage;
import static com.shop.admin.product.ProductSaveHelper.setAndSaveNewExtraImages;
import static com.shop.admin.product.ProductSaveHelper.setProductDetails;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.brand.BrandNotFoundException;
import com.shop.admin.category.CategoryNotFoundException;
import com.shop.admin.user.ShopUserDetails;
import com.shop.admin.brand.BrandService;
import com.shop.admin.category.CategoryService;
import com.shop.dto.ProductDTO;
import com.shop.model.Product;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final BrandService brandService;
  private final CategoryService categoryService;

  @GetMapping
  public String productPage(Model model) {
    var page = productService.findAllProductsSortedBy(null, 1, "id", "asc", 0l);
    changingDisplayProductsPage(1, model, page, "id", "asc", null, 0l);

    return "products/products";
  }

  @GetMapping("/new")
  public String createNewProductPage(Model model) {
    var brands = brandService.findAllByNameAsc();
    var categories = categoryService.listCategoriesHierarchal();
    var dto = ProductDTO.builder()
        .enabled(true)
        .inStock(true)
        .build();
    model.addAttribute("brands", brands);
    model.addAttribute("categories", categories);
    model.addAttribute("productDTO", dto);
    model.addAttribute("pageTitle", "Create new Product");

    return "products/products_form";
  }

  @PostMapping("/save")
  public String createNewProduct(ProductDTO dto,
      @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
      @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImagesMultipart,
      @RequestParam(name = "detailName", required = false) String[] detailNames,
      @RequestParam(name = "detailValue", required = false) String[] detailValues,
      @AuthenticationPrincipal ShopUserDetails loggedUser, RedirectAttributes attributes) {

    try {
      var product = convertToProduct(dto);
      productService.save(product);

      if (loggedUser.hasRole("Salesperson")) {
        productService.saveProductPrice(product);
        attributes.addFlashAttribute("message", "The product has been saved successfully.");
        return "redirect:/api/v1/products";
      }

      setAndSaveMainImage(mainImageMultipart, product);
      setAndSaveNewExtraImages(extraImagesMultipart, product);
      setProductDetails(detailNames, detailValues, product, productService);

      productService.save(product);
      attributes.addFlashAttribute("message", "The product has been saved successfully.");

    } catch (BrandNotFoundException | CategoryNotFoundException | IOException | ProductNotFoundException e) {
      attributes.addFlashAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return "redirect:/api/v1/products";
  }

  @GetMapping("/{pageNum}")
  public String pageByNumber(@PathVariable("pageNum") Integer pageNum,
      @RequestParam("sortField") String sortField,
      @RequestParam("sortDir") String sortDir,
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "categoryId", required = false) Long categoryId,
      Model model) {

    var page = productService.findAllProductsSortedBy(keyword, pageNum, sortField, sortDir, categoryId);
    changingDisplayProductsPage(pageNum, model, page, sortField, sortDir, keyword, categoryId);

    return "products/products";
  }

  @GetMapping("/edit/{id}")
  public String editProduct(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
    try {
      var product = productService.findById(id);
      var brands = brandService.findAllByNameAsc();
      var categories = categoryService.listCategoriesHierarchal();
      var category = product.getCategory();
      var brand = product.getBrand();

      model.addAttribute("brands", brands);
      model.addAttribute("brand", brand);
      model.addAttribute("categories", categories);
      model.addAttribute("category", category);
      model.addAttribute("productDTO", convertToProductDTO(product));
      model.addAttribute("imagesAmount", product.getImages().size());
      model.addAttribute("pageTitle", "Manage Product with ID: " + id);

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
  public String delete(@PathVariable("id") Long id, RedirectAttributes attributes) {
    try {
      productService.deleteProduct(id);
      var uploadDir = "E:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\" + id;
      FileUtils.deleteQuietly(new File(uploadDir));

      attributes.addFlashAttribute("message", "The product with ID: " + id + " has been deleted successfully.");
    } catch (ProductNotFoundException e) {
      attributes.addFlashAttribute("message", e.getMessage());
      e.printStackTrace();
    }

    return "redirect:/api/v1/products";
  }

  @GetMapping("/enabled/true/{id}")
  public String changeToEnableState(@PathVariable("id") Long id, RedirectAttributes attributes) {
    productService.changeProductState(id, true);
    attributes.addFlashAttribute("message", "The product with ID: " + id + " is now enabled.");

    return "redirect:/api/v1/products";
  }

  private void changingDisplayProductsPage(int pageNum, Model model, Page<Product> page, String sortField,
      String sortDir, String keyword, Long categoryId) {

    String reverseSortOrder = sortDir.equals("asc") ? "desc" : "asc";

    model.addAttribute("keyword", keyword);
    model.addAttribute("products", page.getContent());
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("sortField", sortField);
    model.addAttribute("reverseSortOrder", reverseSortOrder);
    model.addAttribute("currentPage", pageNum);
    model.addAttribute("lastPage", (page.getTotalElements() / ProductService.PAGE_SIZE) + 1);
    model.addAttribute("totalPages", page.getTotalElements());
    model.addAttribute("categories", categoryService.listCategoriesHierarchal());

    if (categoryId != null)
      model.addAttribute("categoryId", categoryId);
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

  private Product convertToProduct(ProductDTO dto)
      throws BrandNotFoundException, CategoryNotFoundException, ProductNotFoundException {

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