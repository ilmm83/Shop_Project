package com.shop.site.repository;


import com.common.model.Country;
import com.shop.site.country.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        // given
        var expectedCountry = new Country(1, "China", "CN");
        var expectedResult = Optional.of(expectedCountry);

        willReturn(expectedResult).given(repository).findById(1);

        // when
        var result = repository.findById(1);

        // then
        assertFalse(result.isEmpty());
        assertEquals(expectedCountry, result.get());
    }

    @Test
    void canUpdateCountry() {
        // given
        var expectedCountry = new Country(1, "China", "CN");
        var expectedResult = Optional.of(expectedCountry);
        var expectedUpdatedCountry = new Country(1, "Republic of India", "IN");

        willReturn(expectedResult).given(repository).findById(1);
        willReturn(expectedUpdatedCountry).given(repository).save(expectedUpdatedCountry);

        // when
        var result = repository.findById(1);
        var updatedCountry = repository.save(expectedUpdatedCountry);

        // then
        assertFalse(result.isEmpty());
        assertNotNull(updatedCountry);
        assertEquals(expectedResult, result);
        assertEquals(expectedUpdatedCountry, updatedCountry);
    }

    @Test
    void canDeleteCountry() {
        // given
        var expectedResult = Optional.empty();

        willDoNothing().given(repository).deleteById(1);
        willReturn(expectedResult).given(repository).findById(1);

        // when
        repository.deleteById(1);
        var result = repository.findById(1);

        // then
        verify(repository, times(1)).deleteById(1);
        assertTrue(result.isEmpty());
    }

    @Test
    void canFindAllByOrderByNameAsc() {
        // given
        var expectedCountries = List.of(new Country("China", "CN"));

        willReturn(expectedCountries).given(repository).findAllByOrderByNameAsc();

        // when
        var countries = (List<Country>) repository.findAllByOrderByNameAsc();

        // then
        assertEquals(expectedCountries, countries);
    }
}