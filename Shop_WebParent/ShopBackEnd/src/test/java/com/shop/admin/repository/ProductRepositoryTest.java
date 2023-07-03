package com.shop.admin.repository;

import com.common.model.Product;
import com.shop.admin.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {

    @Spy
    ProductRepository repository;


    @Test
    void canFindByNameAndAlias() {
        // given
        var name = "name";
        var alias = "alias";
        var expectedProducts = List.of(new Product());

        given(repository.findByNameAndAlias(name, alias)).willReturn(expectedProducts);

        // when
        var products = (List<Product>) repository.findByNameAndAlias(name, alias);

        // then
        assertEquals(expectedProducts, products);
    }

    @Test
    void canChangeProductState() {
        // given
        var productId = 1L;

        willDoNothing().given(repository).changeProductState(productId, true);

        // when
        repository.changeProductState(productId, true);

        // then
        verify(repository).changeProductState(productId, true);
    }

    @Test
    void canClearProductDetails() {
        // given
        var productId = 1L;

        willDoNothing().given(repository).clearProductDetails(productId);

        // when
        repository.clearProductDetails(productId);

        // then
        verify(repository).clearProductDetails(productId);
    }

    @Test
    void canRemoveImageByProductId() {
        // given
        var productId = 1L;
        var productImage = "image";

        willDoNothing().given(repository).removeImageByProductId(productId, productImage);

        // when
        repository.removeImageByProductId(productId, productImage);

        // then
        verify(repository).removeImageByProductId(productId, productImage);
    }

    @Test
    void canFindAllInCategory() {
        // given
        var categoryId = 1L;
        var categoryIdMatch = "match";
        var pageable = mock(Pageable.class);
        var expectedPage = new PageImpl<>(List.of(new Product()));

        given(repository.findAllInCategory(categoryId, categoryIdMatch, pageable)).willReturn(expectedPage);

        // when
        var page = repository.findAllInCategory(categoryId, categoryIdMatch, pageable);

        // then
        assertEquals(expectedPage, page);
    }

    @Test
    void canSearchInCategory() {
        // given
        var categoryId = 1L;
        var categoryIdMatch = "match";
        var pageable = mock(Pageable.class);
        var keyword = "keyword";
        var expectedPage = new PageImpl<>(List.of(new Product()));

        given(repository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable)).willReturn(expectedPage);

        // when
        var page = repository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);

        // then
        assertEquals(expectedPage, page);
    }
}
