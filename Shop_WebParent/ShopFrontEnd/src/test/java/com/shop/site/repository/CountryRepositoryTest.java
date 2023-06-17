package com.shop.site.repository;


import com.common.model.Country;
import com.shop.site.country.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class CountryRepositoryTest {

    @Spy
    CountryRepository repository;


    @Test
    void canCreateCountry() {
        // given
        var expectedCountry = new Country("China", "CN");

        willReturn(expectedCountry).given(repository).save(expectedCountry);

        // when
        var country = repository.save(expectedCountry);

        // then
        assertEquals(expectedCountry, country);
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