package com.shop.admin.repository.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.Brand;

@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Long> {

  Brand save(Brand brand);

  java.lang.Iterable<Brand> saveAll(java.lang.Iterable<Brand> brands);

  Brand findById(long id);

  java.lang.Iterable<Brand> findAll();

  Brand delete(Brand brand);

  @Query("SELECT b FROM Brand b WHERE CONCAT(b.id, ' ', b.name) LIKE %?1%")
  Page<Brand> findAll(String keyword, Pageable pageable);
}
