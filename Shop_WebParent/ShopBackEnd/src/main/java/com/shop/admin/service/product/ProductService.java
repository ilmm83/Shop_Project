
package com.shop.admin.service.product;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.admin.exception.product.ProductNotFoundException;
import com.shop.admin.repository.product.ProductRepository;
import com.shop.model.Product;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository repository;

  public static final int PAGE_SIZE = 4;

  public Page<Product> findAllProductsSortedBy(String keyword, int pageNum, String field, String direction,
      Long categoryId) {
    Sort sort = Sort.by(field);
    sort = direction.equals("asc") ? sort.ascending() : sort.descending();

    PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

    if (keyword != null && !keyword.isEmpty()) {
      if (categoryId != null && categoryId > 0) {
        var categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        return repository.searchInCategory(categoryId, categoryIdMatch,keyword, pageable);
      } else
        return repository.findAll(keyword, pageable);
    } else if (categoryId != null && categoryId > 0) {
      var categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
      return repository.findAllInCategory(categoryId, categoryIdMatch, pageable);
    } else
      return repository.findAll(pageable);
  }

  @Transactional
  public Product save(Product product) {
    if (product.getId() == null)
      product.setCreatedAt(new Date());

    if (product.getAlias() == null || product.getAlias().isEmpty())
      product.setAlias(product.getName().replaceAll(" ", "-"));
    else
      product.setAlias(product.getAlias().replaceAll(" ", "-"));

    product.setUpdatedAt(new Date());

    return repository.save(product);
  }

  @Transactional
  public void saveProductPrice(Product productFromForm) {
    var productInDB = repository.findById(productFromForm.getId());
    productInDB.setPrice(productFromForm.getPrice());
    productInDB.setCost(productFromForm.getCost());
    productInDB.setDiscountPercent(productFromForm.getDiscountPercent());

    repository.save(productInDB);
  }

  @Transactional
  public void clearProductDetails(Long id) {
    repository.clearProductDetails(id);
  }

  @Transactional
  public void changeProductState(Long id, boolean state) {
    repository.changeProductState(id, state);
  }

  @Transactional
  public void deleteProduct(Long id) throws ProductNotFoundException {
    var counted = repository.findById(id);
    if (counted == null)
      throw new ProductNotFoundException("Could not find the product with id: " + id);

    repository.deleteById(id);
  }

  @Transactional
  public void removeImage(Long productId, String fileName) {
    repository.removeImageByProductId(productId, fileName);
    var uploadDir = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\" + productId
        + "\\extras\\" + fileName;
    FileUtils.deleteQuietly(new File(uploadDir));
  }

  public String checkNameUnique(Long id, String name) {
    var products = repository.findByName(name);
    var response = "OK";

    for (var product : products) {
      if (!response.equals("OK"))
        break;
      if (product == null)
        continue;
      response = isProductExistsByName(id, product, name);
    }
    return response;
  }

  private String isProductExistsByName(Long id, Product product, String name) {
    if (product == null || product.getId() == id)
      return "OK";
    else if (product.getName().equals(name))
      return "product";
    return "OK";
  }

  public Product findById(Long id) throws ProductNotFoundException {
    var product = repository.findById(id);
    if (product == null)
      throw new ProductNotFoundException("Could not found product with ID: " + id);
    return product;
  }

}
