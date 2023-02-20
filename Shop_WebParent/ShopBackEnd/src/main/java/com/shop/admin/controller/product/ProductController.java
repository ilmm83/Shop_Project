package com.shop.admin.controller.product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.exception.brand.BrandNotFoundException;
import com.shop.admin.exception.category.CategoryNotFoundException;
import com.shop.admin.exception.product.ProductNotFoundException;
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
  public String createNewProduct(ProductDTO dto, @RequestParam("fileImage") MultipartFile mainImageMultipart,
      @RequestParam("extraImage") MultipartFile[] extraMultipart,
      @RequestParam(name = "detailName", required = false) String[] detailNames,
      @RequestParam(name = "detailValue", required = false) String[] detailValues,
      Model model, RedirectAttributes attributs) throws BrandNotFoundException {

    try {
      var product = convertToProduct(dto);
      setMainImage(mainImageMultipart, product);
      setExtraImages(extraMultipart, product);
      setProductDetails(detailNames, detailValues, product);
      saveUploadedImages(extraMultipart, mainImageMultipart, product);

      attributs.addFlashAttribute("message", "The product has been saved successfully.");
      productService.save(product);
      
    } catch (BrandNotFoundException | CategoryNotFoundException | IOException e) {
      attributs.addFlashAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return "redirect:/api/v1/products";
  }

  @GetMapping("/{id}/enabled/false")
  public String changeToDisableState(@PathVariable("id") Long id, RedirectAttributes attributes) {
    productService.changeProductState(id, false);
    attributes.addFlashAttribute("message", "The product with ID: " + id + " is now disabled.");

    return "redirect:/api/v1/products";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable("id") Long id, RedirectAttributes attributes) {
    try {
      productService.deleteProduct(id);
      var uploadDir = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\" + id;
      FileUtils.deleteQuietly(new File(uploadDir));

      attributes.addFlashAttribute("message", "The product with ID: " + id + " has been deleted successfully.");
    } catch (ProductNotFoundException e) {
      attributes.addFlashAttribute("message", e.getMessage());
      e.printStackTrace();
    }

    return "redirect:/api/v1/products";
  }

  @GetMapping("/{id}/enabled/true")
  public String changeToEnableState(@PathVariable("id") Long id, RedirectAttributes attributes) {
    productService.changeProductState(id, true);
    attributes.addFlashAttribute("message", "The product with ID: " + id + " is now enabled.");

    return "redirect:/api/v1/products";
  }

  private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
    if (detailNames == null || detailNames.length == 0)
      return;

    for (int i = 0; i < detailNames.length; i++) {
      var name = detailNames[i];
      var value = detailValues[i];

      if (!name.isEmpty() && !value.isEmpty())
        product.addDetail(name, value);
    }
  }

  private void saveUploadedImages(MultipartFile[] extraImages, MultipartFile mainImage, Product product)
      throws IOException {
    if (mainImage.isEmpty())
      return;

    var saved = productService.save(product);
    var fileName = StringUtils.cleanPath(mainImage.getOriginalFilename());
    var uploadDir = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\" + saved.getId();
    FileUploadUtil.saveFile(uploadDir, fileName, mainImage);

    if (extraImages.length == 0)
      return;

    var uploadDirForExtras = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\"
        + saved.getId() + "\\extras";
    for (var imageMP : extraImages) {
      if (imageMP.isEmpty())
        continue;
      var imageName = StringUtils.cleanPath(imageMP.getOriginalFilename());
      FileUploadUtil.saveFileWithoutClearingForlder(uploadDirForExtras, imageName, imageMP);
    }
  }

  private void setExtraImages(MultipartFile[] multipart, Product product) {
    if (multipart.length == 0)
      return;

    for (var file : multipart) {
      if (file.isEmpty())
        continue;
      var fileName = StringUtils.cleanPath(file.getOriginalFilename());
      product.addExtraImage(fileName);
    }
  }

  private void setMainImage(MultipartFile multipart, Product product) {
    if (!multipart.isEmpty()) {
      var fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
      product.setMainImage(fileName);
    }
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
    var category = dto.getCategoryId() == 0 ? null : categoryService.findById(dto.getCategoryId());

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