package com.shop.site.repository;

import com.common.model.Category;
import com.shop.site.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
public class CategoryRepositoryTest {

    @Spy
    CategoryRepository repository;


    @Test
    void canFindAllEnabledCategories() {
        // given
        var expectedCategory = new Category();
        expectedCategory.setEnabled(true);
        var expectedResult = List.of(expectedCategory);

        willReturn(expectedResult).given(repository).findAllEnabledCategories();

        // when
        var result = repository.findAllEnabledCategories();

        // then
        assertEquals(expectedResult, result);
        assertEquals(expectedResult.size(), result.size());
        assertEquals(expectedCategory, result.get(0));
        assertTrue(result.get(0).isEnabled());
    }

    @Test
    void canFindByAliasEnabled() {
        // given
        var expectedAlias = "test-alias";
        var expectedCategory = new Category();
        expectedCategory.setEnabled(true);
        expectedCategory.setAlias(expectedAlias);
        var expectedResult = Optional.of(expectedCategory);

        willReturn(expectedResult).given(repository).findByAliasEnabled(expectedAlias);

        // when
        var result = repository.findByAliasEnabled(expectedAlias);

        // then
        assertFalse(result.isEmpty());
        assertEquals(expectedCategory, result.get());
        assertEquals(expectedAlias, result.get().getAlias());
        assertTrue(result.get().isEnabled());
    }
}