package com.shop.site.prouduct;

import com.common.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p"
            + " WHERE p.enabled = true"
            + " AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)"
            + " ORDER BY p.name ASC")
    Page<Product> listByCategory(Long categoryId, String categoryIDMatch, Pageable pageable);

    @Query(value =
            "SELECT * FROM products "
            + " WHERE enabled = true"
            + " AND MATCH(name, short_description, full_description) AGAINST (?1)",
    nativeQuery = true)
    Page<Product> findByKeyword(String keyword, Pageable pageable);

    Optional<Product> findByAlias(String alias);
}
