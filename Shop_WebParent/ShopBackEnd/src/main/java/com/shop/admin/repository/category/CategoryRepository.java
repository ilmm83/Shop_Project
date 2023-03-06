package com.shop.admin.repository.category;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

  Category save(Category category);

  java.lang.Iterable<Category> saveAll(java.lang.Iterable<Category> categories);

  Optional<Category> findById(long id);
  
  java.lang.Iterable<Category> findAll();

  java.lang.Iterable<Category> findAll(Sort sort);
  
  Page<Category> findAll(Pageable pageable);
  
  Optional<Long> countById(Long id);
  
  void deleteById(Long id);

  @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
  @Modifying
  void updateEnabledStatus(Long id, boolean enabled);

  @Query("SELECT c FROM Category c WHERE CONCAT(c.id, ' ', c.name, ' ', c.alias) LIKE %?1%")
  Page<Category> findAll(String keyword, Pageable pageable);

  @Query("SELECT c FROM Category c WHERE c.name LIKE %?1% OR c.alias LIKE %?2%")
  java.lang.Iterable<Category> findByNameAndAlias(String name, String alias);

}
