package com.shop.admin.repository.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shop.admin.service.category.CategoryService;
import com.shop.model.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

  @Mock
  private CategoryRepository repository;

  @InjectMocks
  private CategoryService service;

  private Category cat;

  @BeforeEach
  public void BeforeAll() {
    service = new CategoryService(repository);
    cat = Category.builder()
        .id(7L)
        .name("Computer")
        .alias("sjdfghsdh")
        .build();
  }

  @Test
  public void canSaveTheCategory() {
    when(repository.findByNameAndAlias(cat.getName(), cat.getAlias())).thenReturn(List.of(cat));

    String response = service.checkNameAndAliasUnique(cat.getId(), cat.getName(), cat.getAlias());

    assertThat(response).isEqualTo("OK");
  }

  @Test
  public void canCheckUniqueInNewModelReturnDuplicateNameOrAlias() {
    var catWithDuplName = Category.builder()
        .id(8L)
        .name("Computer")
        .alias("alias")
        .build();

    when(repository.findByNameAndAlias(cat.getName(), cat.getAlias())).thenReturn(List.of(catWithDuplName));

    var response = service.checkNameAndAliasUnique(cat.getId(), cat.getName(), cat.getAlias());

    assertThat(response).isEqualTo("Name");
  }

  @Test
  public void canEditAndCheckUniqueInNewModelReturnDuplicateNameOrAlias() {
    var updatedCat = Category.builder()
        .id(7L)
        .name("Computer")
        .alias("Computer")
        .build();

    when(repository.findByNameAndAlias(cat.getName(), cat.getAlias())).thenReturn(List.of(updatedCat));

    var response = service.checkNameAndAliasUnique(cat.getId(), cat.getName(), cat.getAlias());

    assertThat(response).isEqualTo("OK");
  }

}
