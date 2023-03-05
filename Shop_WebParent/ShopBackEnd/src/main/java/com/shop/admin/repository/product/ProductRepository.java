package com.shop.admin.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    
    // todo: change all repo's methods to return Optional<> and refactor the attached code.

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

  @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
  @Modifying
  void changeProductState(Long id, boolean state);

  @Query("DELETE FROM ProductDetail pd" +
      " WHERE pd.product.id =" +
      " (SELECT p.id FROM Product p WHERE p.id=?1)")
  @Modifying
  void clearProductDetails(Long id);

  @Query("DELETE FROM ProductImage pi" +
      " WHERE pi.name=?2 AND pi.product.id =" +
      " (SELECT p.id FROM Product p WHERE p.id=?1)")
  @Modifying
  void removeImageByProductId(Long productId, String fileName);

  @Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%")
  Page<Product> findAllInCategory(Long categoryId, String categoryIdMatch, Pageable pageable);

  @Query("SELECT p FROM Product p WHERE " 
      + "(p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) "
      + "AND (p.name LIKE %?3% "
      + "OR p.shortDescription LIKE %?3% "
      + "OR p.fullDescription LIKE %?3% "
      + "OR p.brand.name LIKE %?3% "
      + "OR p.category.name LIKE %?3%)")
  Page<Product> searchInCategory(Long categoryId, String categoryIDMatch, String keyword, Pageable pageable);
}
