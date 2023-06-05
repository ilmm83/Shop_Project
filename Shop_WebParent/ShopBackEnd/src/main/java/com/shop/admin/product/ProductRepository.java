package com.shop.admin.product;

import com.shop.admin.paging.SearchRepository;
import com.common.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends SearchRepository<Product, Long> {
    
  Optional<Product> save(Product product);

  Optional<Product> findById(long id);

  Optional<Product> delete(Product product);

  Optional<Product> deleteById(Long id);

  Iterable<Product> saveAll(Iterable<Product> products);

  Iterable<Product> findAll();

  Optional<Long> countById(Long id);

  @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% OR p.alias LIKE %?2%")
  Iterable<Product> findByNameAndAlias(String name, String alias);

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
