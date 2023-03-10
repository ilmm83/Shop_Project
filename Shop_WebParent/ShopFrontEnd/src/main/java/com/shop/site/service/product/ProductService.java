package com.shop.site.service.product;

import com.shop.model.Product;
import com.shop.site.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public static final int PRODUCTS_PER_PAGE = 10;

    public Page<Product> listByCategory(int pageNum, Long categoryId) {
        var categoryIDMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum, PRODUCTS_PER_PAGE);

        return repository.listByCategory(categoryId, categoryIDMatch, pageable);
    }

}
