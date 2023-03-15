package com.shop.site.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback(true)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void canListByCategory() {
        var cats = repository.listByCategory(1L, null, Pageable.unpaged());

        assertThat(cats).isNotNull();
    }

    @Test
    void canFindByAlias() {
        var computers = repository.findByAlias("Acer").get();

        assertThat(computers.getAlias()).isEqualTo("Acer");
    }
}