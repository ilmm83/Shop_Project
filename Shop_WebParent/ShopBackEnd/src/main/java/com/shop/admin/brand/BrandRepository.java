package com.shop.admin.brand;

import com.common.model.Brand;
import com.shop.admin.paging.SearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends SearchRepository<Brand, Long> {

    Optional<Brand> save(Brand brand);

    Optional<Brand> findById(long id);

    Optional<Brand> delete(Brand brand);

    Optional<Long> countById(Long id);

    Brand deleteById(Long id);

    Iterable<Brand> saveAll(Iterable<Brand> brands);

    Iterable<Brand> findAll();

    @Query("SELECT b FROM Brand b ORDER BY b.name ASC")
    Iterable<Brand> findAllByNameAsc();

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    Iterable<Brand> findByName(String name);
}
