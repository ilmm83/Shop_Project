package com.shop.site.category;

import com.common.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
