package com.shop.admin.repository.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shop.model.Brand;
import com.shop.model.Category;
import com.shop.model.Product;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)
public class ProductRepositoryTest {

  @Autowired
  private ProductRepository repository;

  @Autowired
  private TestEntityManager manager;

  @Test
  void canCreateProducts() {
    var brand = manager.find(Brand.class, 16);
    var categegory = manager.find(Category.class, 8);
    var product = Product.builder()
        .name("Samsung Galaxy A31")
        .alias("Samsung")
        .shortDescription("short description")
        .fullDescription("full description")
        .brand(brand)
        .category(categegory)
        .enabled(true)
        .inStock(true)
        .price(new BigDecimal(456))
        .discountPercent(new BigDecimal(0))
        .cost(new BigDecimal(456))
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();
    var saved = repository.save(product);

    assertThat(product).isNotNull();
    assertThat(product).isEqualTo(saved);
  }

  @Test
  void canFindAll() {
    var brands = (List<Product>) repository.findAll();

    assertThat(brands).isNotNull();
  }

  @Test
  void canFindById() {
    var product = repository.findById(3L);
    assertThat(product.getName()).isEqualTo("Samsung Galaxy A31");
  }

  @Test
  void canUpdateProduct() {
    var product = repository.findById(3L);
    product.setAlias("Samsung A31");
    var saved = repository.save(product);

    assertThat(saved.getAlias()).isEqualTo("Samsung A31");
  }

  @Test
  void canSaveProductWithImages() {
    var product = repository.findById(3L);
    product.setMainImage("main Image");
    product.addExtraImage("main image 2");

    var saved = repository.save(product);

    assertThat(saved.getImages().size()).isEqualTo(1);
    assertThat(saved.getMainImage()).isEqualTo("main Image");
  }

  @Test
  void canSaveProductWithDetails() {
    var product = repository.findById(3L);
    product.addDetail("Device Memory", "128 GB");

    var saved = repository.save(product);

    assertThat(saved.getDetails().size()).isEqualTo(1);
  }
}
