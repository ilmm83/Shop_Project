package com.shop.admin.repository.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import com.shop.admin.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shop.admin.category.CategoryService;
import com.common.model.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    private Category category;

    @BeforeEach
    public void BeforeAll() {
        service = new CategoryService(repository);

        // given
        category = Category.builder()
                .id(7L)
                .name("Computer")
                .alias("sjdfghsdh")
                .build();
    }

    @Test
    public void canSaveTheCategory() {
        // when
        when(repository.findByNameAndAlias(category.getName(), category.getAlias())).thenReturn(List.of(category));

        String response = service.checkNameAndAliasUnique(category.getId(), category.getName(), category.getAlias());

        // then
        assertThat(response).isEqualTo("OK");
    }

    @Test
    public void canCheckUniqueInNewModelReturnDuplicateNameOrAlias() {
        // given
        var catWithDuplName = Category.builder()
                .id(8L)
                .name("Computer")
                .alias("alias")
                .build();

        // when
        when(repository.findByNameAndAlias(category.getName(), category.getAlias())).thenReturn(List.of(catWithDuplName));

        var response = service.checkNameAndAliasUnique(category.getId(), category.getName(), category.getAlias());

        // then
        assertThat(response).isEqualTo("Name");
    }

    @Test
    public void canEditAndCheckUniqueInNewModelReturnDuplicateNameOrAlias() {
        // given
        var updatedCat = Category.builder()
                .id(7L)
                .name("Computer")
                .alias("Computer")
                .build();

        // when
        when(repository.findByNameAndAlias(category.getName(), category.getAlias())).thenReturn(List.of(updatedCat));

        var response = service.checkNameAndAliasUnique(category.getId(), category.getName(), category.getAlias());

        // then
        assertThat(response).isEqualTo("OK");
    }

}
