package com.shop.site.repository;

import com.common.model.Product;
import com.shop.site.prouduct.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Spy
    ProductRepository repository;


    @Test
    void canListByCategory() {
        // given
        var expectedPageContent = List.of(new Product());
        var expectedPage = new PageImpl<>(expectedPageContent);
        var categoryIDMatch = "category-id-match";
        var pageable = mock(Pageable.class);

        willReturn(expectedPage).given(repository).listByCategory(1L, categoryIDMatch, pageable);

        // when
        var page = repository.listByCategory(1L, categoryIDMatch, pageable);

        // then
        assertEquals(expectedPage, page);
        assertEquals(expectedPageContent, page.getContent());
    }

    @Test
    void canFindByAlias() {
        var computers = repository.findByAlias("Acer").get();

        assertThat(computers.getAlias()).isEqualTo("Acer");
    }
}