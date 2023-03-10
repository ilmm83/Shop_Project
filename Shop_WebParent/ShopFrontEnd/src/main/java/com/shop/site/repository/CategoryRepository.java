package com.shop.site.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    @Query("SELECT c FROM Category c"
            + " WHERE c.enabled = true"
            + " ORDER BY c.name ASC")
    List<Category> findAllEnabledCategories();

    @Query("SELECT c FROM Category c"
            + " WHERE c.enabled = true AND c.alias = ?1")
    Optional<Category> findByAliasEnabled(String alias);

}
