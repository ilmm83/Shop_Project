package com.shop.site.repository;

import com.common.model.Category;
import com.shop.site.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest(showSql = false, bootstrapMode = BootstrapMode.LAZY)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback(true)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    void canFindAllEnabledCategories() {
        repository.findAllEnabledCategories()
                .forEach(category ->
                        System.out.println(category.getName() + " ( " + category.isEnabled() + " )"));
    }

    @Test
    void canFindByAliasEnabled() {
        Category cameras = repository.findByAliasEnabled("Cameras").get();

        assertThat(cameras).isNotNull();
    }
}