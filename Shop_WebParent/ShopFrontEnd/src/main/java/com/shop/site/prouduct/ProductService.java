package com.shop.site.prouduct;

import com.shop.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repository;

    public static final int PRODUCTS_PER_PAGE = 10;
    public static final int SEARCH_PER_PAGE = 10;

    public Page<Product> listByCategory(int pageNum, Long categoryId) {
        var categoryIDMatch = "-" + categoryId + "-";
        Pageable pageable = PageRequest.of(pageNum, PRODUCTS_PER_PAGE);

        return repository.listByCategory(categoryId, categoryIDMatch, pageable);
    }

    public Product getProductByAlias(String alias)  {
        return repository.findByAlias(alias)
                .orElseThrow(() -> new ProductNotFoundException("Could not find a product with alias: " + alias));
    }

    public Page<Product> searchByKeyword(String keyword, int pageNum) {
        var pageable = PageRequest.of(pageNum - 1, SEARCH_PER_PAGE);
        return repository.findByKeyword(keyword, pageable);
    }
}
