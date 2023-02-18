package com.shop.admin.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

  Product save(Product product);

  Product findById(long id);

  Product delete(Product product);

  Product deleteById(Long id);

  java.lang.Iterable<Product> saveAll(java.lang.Iterable<Product> products);

  java.lang.Iterable<Product> findAll();

  Long countById(Long id);

  @Query("SELECT p FROM Product p WHERE CONCAT(p.id, ' ', p.name) LIKE %?1%")
  Page<Product> findAll(String keyword, Pageable pageable);

  @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
  java.lang.Iterable<Product> findByName(String name);

}
