package com.shop.admin.category;

import com.common.model.Category;
import com.shop.admin.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends SearchRepository<Category, Long> {

    Category save(Category category);

    Optional<Category> findById(long id);

    Optional<Long> countById(Long id);

    Iterable<Category> findAll(Sort sort);

    Page<Category> findAll(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1% OR c.alias LIKE %?2%")
    Iterable<Category> findByNameAndAlias(String name, String alias);

    @Modifying
    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    void updateEnabledStatus(Long id, boolean enabled);

    void deleteById(Long id);
}
