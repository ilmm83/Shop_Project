package com.shop.admin.service.product;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  public String checkNameUnique(Long id, String name) {
    var brands = repository.findByName(name);
    var response = "OK";

    for (var brand : brands) {
      if (!response.equals("OK"))
        break;
      if (brand == null)
        continue;
      response = isProductExistsByName(id, brand, name);
    }
    return response;
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

  private String isProductExistsByName(Long id, Product product, String name) {
    if (product == null || product.getId() == id)
      return "OK";
    else if (product.getName().equals(name))
      return "Name";
    return "OK";
  }

}
