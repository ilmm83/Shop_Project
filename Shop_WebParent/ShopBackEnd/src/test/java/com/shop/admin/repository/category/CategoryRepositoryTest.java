package com.shop.admin.repository.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;

import com.shop.admin.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.common.model.Category;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback()
@Slf4j
public class CategoryRepositoryTest {

  @Autowired
  private CategoryRepository repository;

  @Test
  public void canCreateRootCategory() {
    var computers = Category.builder()
        .id(1l)
        .name("Computers")
        .alias("Computers")
        .image("default.png")
        .enabled(true)
        .parent(null)
        .build();

    var electronics = Category.builder()
        .id(2l)
        .name("Electronics")
        .alias("Electronics")
        .image("default.png")
        .enabled(true)
        .parent(null)
        .build();

    var list = List.of(computers, electronics);
    var categories = (List<Category>) repository.saveAll(list);
    assertThat(categories.size()).isPositive();
  }

  @Test
  public void canCreateSubCategory() {
    var parent = repository.findById(1L).get();

    var category = Category.builder()
        .name("Iphone")
        .alias("Iphone")
        .image("default.png")
        .enabled(true)
        .parent(parent)
        .build();

    var categories = (List<Category>) repository.saveAll(List.of(category));

    assertThat(categories.size()).isPositive();
  }

  @Test
  public void canGetCategory() {
    var category = repository.findById(6l);

    log.info(category.toString());

    assertThat(category).isNotNull();
  }

  @Test
  public void canPrintHierarchicalCategories() {
    Iterable<Category> categories = repository.findAll();

    for (Category cat : categories) {
      if (cat.getParent() == null) {
        log.info(cat.getName());
        for (var sub : cat.getChildren()) {
          log.info("--" + sub.getName());
          printChildren(sub);
        }
      }
    }
  }

  private void printChildren(Category parent) {
    var children = parent.getChildren();
    var dashes = new StringBuilder("--");
    for (Category sub : children) {
      dashes.append("--".repeat(children.size()));
      log.info(dashes + sub.getName());
      printChildren(sub);
      children.remove(sub);
    }
  }
}
