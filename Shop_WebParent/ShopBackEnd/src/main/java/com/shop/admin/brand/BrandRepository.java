package com.shop.admin.brand;

import com.shop.admin.paging.SearchRepository;
import com.shop.model.Brand;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends SearchRepository<Brand, Long> {

  Optional<Brand> save(Brand brand);

  Optional<Brand> findById(long id);

  Optional<Brand> delete(Brand brand);

  Brand deleteById(Long id);

  java.lang.Iterable<Brand> saveAll(java.lang.Iterable<Brand> brands);

  java.lang.Iterable<Brand> findAll();

  Optional<Long> countById(Long id);

  @Query("SELECT b FROM Brand b ORDER BY b.name ASC")
  java.lang.Iterable<Brand> findAllByNameAsc();

  @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
  java.lang.Iterable<Brand> findByName(String name);
}
