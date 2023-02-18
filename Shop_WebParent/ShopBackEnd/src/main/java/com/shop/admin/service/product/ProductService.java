package com.shop.admin.service.product;

import java.util.Date;

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

  public Page<Product> findAllProductsSortedBy(String keyword, int pageNum, String field, String direction) {
    Sort sort = Sort.by(field);
    sort = direction.equals("asc") ? sort.ascending() : sort.descending();

    PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

    if (keyword != null)
      return repository.findAll(keyword, pageable);
    else
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

}
