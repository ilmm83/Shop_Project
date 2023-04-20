package com.shop.admin.brand;

import java.util.Optional;

import com.shop.admin.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.Brand;

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
