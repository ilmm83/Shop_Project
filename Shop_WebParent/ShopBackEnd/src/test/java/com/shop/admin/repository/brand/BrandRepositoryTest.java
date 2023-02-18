package com.shop.admin.repository.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.admin.repository.category.CategoryRepository;
import com.shop.model.Brand;
import com.shop.model.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)
public class BrandRepositoryTest {

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void canCreateBrand() {
    var laptops = (List<Category>) categoryRepository.findByNameAndAlias("Laptops", "Laptops");
    var cellPhones = (List<Category>) categoryRepository.findByNameAndAlias("Cell phones & Accessories", "Cell phones & Accessories");
    var tablets = (List<Category>) categoryRepository.findByNameAndAlias("Tablets", "Tablets");
    var hd = (List<Category>) categoryRepository.findByNameAndAlias("Internal Hard Drives","Internal Hard Drives");

    
    var acer = Brand.builder()
        .name("Acer")
        .logo("default.png")
        .categories(Set.of(laptops.get(0)))
        .build();
    var apple = Brand.builder()
        .name("Apple")
        .logo("default.png")
        .categories(Set.of(cellPhones.get(0), tablets.get(0)))
        .build();
    var samsung = Brand.builder()
        .name("Samsung")
        .logo("default.png")
        .categories(Set.of(hd.get(0)))
        .build();
    var brands = (List<Brand>) brandRepository.saveAll(List.of(acer, apple, samsung));

    assertThat(brands.size()).isEqualTo(3L);

  }

  @Test
  void canFindAll() {
      var brands = (List<Brand>) brandRepository.findAll();

      assertThat(brands).isNotNull();
  }

  @Test
  void canFindById() {
    var acer = brandRepository.findById(14L);
    assertThat(acer.getName()).isEqualTo("Acer");
  }

}
