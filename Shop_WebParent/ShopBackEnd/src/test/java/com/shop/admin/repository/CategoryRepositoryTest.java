package com.shop.admin.repository;

import com.common.model.Category;
import com.shop.admin.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryRepositoryTest {

    @Spy
    CategoryRepository repository;


    @Test
    void canFindByNameAndAlias() {
        // given
        var name = "name";
        var alias = "alias";
        var expectedCategories = List.of(new Category());

        willReturn(expectedCategories).given(repository).findByNameAndAlias(name, alias);

        // when
        var categories = repository.findByNameAndAlias(name, alias);

        // then
        assertEquals(expectedCategories, categories);
    }

    @Test
    void canUpdateEnabledStatus() {
        // given
        var categoryId = 1L;

        willDoNothing().given(repository).updateEnabledStatus(categoryId, true);

        // when
        repository.updateEnabledStatus(categoryId, true);

        // then
        verify(repository).updateEnabledStatus(categoryId, true);
    }
}
