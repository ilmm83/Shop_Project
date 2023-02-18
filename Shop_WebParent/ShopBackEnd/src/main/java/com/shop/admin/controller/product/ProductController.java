package com.shop.admin.controller.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.exception.brand.BrandNotFoundException;
import com.shop.admin.exception.category.CategoryNotFoundException;
import com.shop.admin.service.brand.BrandService;
import com.shop.admin.service.category.CategoryService;
import com.shop.admin.service.product.ProductService;
import com.shop.admin.utils.FileUploadUtil;
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
    var page = productService.findAllProductsSortedBy(null, 1, "id", "asc");
    changingDisplayProductsPage(1, model, page, "id", "asc", null);

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

    return "products/products_form";
  }

  @PostMapping("/new")
  public String createNewProduct(ProductDTO dto, MultipartFile multipart, Model model, RedirectAttributes attributs)
      throws BrandNotFoundException {

    try {
      var product = convertToProduct(dto);
      var saved = productService.save(product);
      
      attributs.addAttribute("message", "The product has been saved successfully.");
    } catch (BrandNotFoundException | CategoryNotFoundException e) {
      attributs.addAttribute("message", e.getMessage());
      e.printStackTrace();
      throw new BrandNotFoundException(e.getMessage());
    }
    return "redirect:/api/v1/products";
  }

  private void changingDisplayProductsPage(int pageNum, Model model, Page<Product> page, String sortField,
      String sortDir,
      String keyword) {

    String reverseSortOrder = sortDir.equals("asc") ? "desc" : "asc";

    model.addAttribute("keyword", keyword);
    model.addAttribute("products", page.getContent());
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("sortField", sortField);
    model.addAttribute("reverseSortOrder", reverseSortOrder);
    model.addAttribute("currentPage", pageNum);
    model.addAttribute("lastPage", (page.getTotalElements() / ProductService.PAGE_SIZE) + 1);
    model.addAttribute("totalProducts", page.getTotalElements());
  }

  private Product convertToProduct(ProductDTO dto) throws BrandNotFoundException, CategoryNotFoundException {
    var product = new Product();
    var brand = dto.getBrandId() == 0 ? null : brandService.findById(dto.getBrandId());
    var category = dto.getCategoryId() == 0 ? null :categoryService.findById(dto.getCategoryId());

    product.setId(dto.getId());
    product.setName(dto.getName());
    product.setAlias(dto.getAlias());
    product.setShortDiscription(dto.getShortDescription());
    product.setFullDiscription(dto.getFullDescription());
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
    product.setBrand(brand);
    product.setCategory(category);

    return product;
  }
}
