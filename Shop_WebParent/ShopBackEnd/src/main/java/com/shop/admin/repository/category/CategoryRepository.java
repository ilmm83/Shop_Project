package com.shop.admin.repository.category;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.Category;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long>{

  Category save(Category root);

  java.lang.Iterable<Category> saveAll(java.lang.Iterable<Category> categories);

  Category findById(long id);

  java.lang.Iterable<Category> findAll();
  

}
