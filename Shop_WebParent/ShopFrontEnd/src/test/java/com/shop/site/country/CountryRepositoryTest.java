package com.shop.site.country;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback
class CountryRepositoryTest {

    @Autowired
    private CountryRepository repository;

    @Test
    void canCreateCountry() {
        var country = repository.save(new Country("China", "CN"));

        assertThat(country).isNotNull();
        assertThat(country.getName()).isEqualTo("China");
    }

    @Test
    void canReadCountry() {
        var country = repository.findById(1).get();

        assertThat(country).isNotNull();
        assertThat(country.getName()).isEqualTo("China");
    }

    @Test
    void canUpdateCountry() {
        var country = repository.findById(1).get();
        country.setName("Republic of India");
        country.setCode("IN");

        var saved = repository.save(country);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Republic of India");
    }

    @Test
    void canDeleteCountry() {
        repository.deleteById(1);
        var deleted = repository.findById(1);

        assertThat(deleted.isEmpty()).isTrue();
    }

    @Test
    void canFindAllByOrderByNameAsc() {
        var countries = (List<Country>) repository.findAllByOrderByNameAsc();

        assertThat(countries).isNotNull();
        assertThat(countries.size()).isGreaterThan(0);
    }
}