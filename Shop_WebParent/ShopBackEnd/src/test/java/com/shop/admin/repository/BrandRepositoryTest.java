package com.shop.admin.repository;

import com.common.model.Brand;
import com.shop.admin.brand.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BrandRepositoryTest {

    @Spy
    BrandRepository repository;


    @Test
    void canSave() {
        // given
        var brand = new Brand();
        var expectedResult = Optional.of(brand);

        given(repository.save(brand)).willReturn(expectedResult);

        // when
        var result = repository.save(brand);

        // then
        assertFalse(result.isEmpty());
        assertEquals(expectedResult, result);
        assertEquals(brand, result.get());
    }

    @Test
    void canFindById() {
        // given
        var brand = new Brand();
        brand.setId(1L);
        var expectedResult = Optional.of(brand);

        given(repository.findById(1L)).willReturn(expectedResult);

        // when
        var result = repository.findById(1L);

        // then
        assertFalse(result.isEmpty());
        assertEquals(expectedResult, result);
        assertEquals(brand, result.get());

    }

    @Test
    void canDelete() {
        // given
        var brand = new Brand();
        brand.setId(1L);
        var expectedResult = Optional.of(brand);

        given(repository.delete(brand)).willReturn(expectedResult);
        given(repository.findById(1L)).willReturn(null);

        // when
        var result = repository.delete(brand);
        var found = repository.findById(1L);

        // then
        assertNull(found);
        assertFalse(result.isEmpty());
        assertEquals(expectedResult, result);
        assertEquals(brand, result.get());
    }

    @Test
    void canCountById() {
        // given
        var expectedResult = Optional.of(1L);

        given(repository.countById(1L)).willReturn(expectedResult);

        // when
        var result = repository.countById(1L);

        // then
        assertFalse(result.isEmpty());
        assertEquals(expectedResult, result);
        assertEquals(expectedResult.get().longValue(), result.get().longValue());
    }

    @Test
    void canDeleteById() {
        // given
        var expectedBrand = new Brand();
        expectedBrand.setId(1L);

        given(repository.deleteById(1L)).willReturn(expectedBrand);
        given(repository.findById(1L)).willReturn(null);

        // when
        var deletedBrand = repository.deleteById(1L);
        var found = repository.findById(1L);

        // then
        assertNull(found);
        assertEquals(expectedBrand, deletedBrand);
    }

    @Test
    void canFindAllByNameAsc() {
        // given
        var expectedResult = List.of(new Brand());

        given(repository.findAllByNameAsc()).willReturn(expectedResult);

        // when
        var result = (List<Brand>) repository.findAllByNameAsc();

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void canFindByName() {
        // given
        var expectedName = "name";
        var expectedBrand = new Brand();
        expectedBrand.setName(expectedName);
        var expectedResult = List.of(expectedBrand);

        given(repository.findByName(expectedName)).willReturn(expectedResult);

        // when
        var result = (List<Brand>) repository.findByName(expectedName);

        // then
        assertEquals(expectedResult, result);
        assertEquals(1, result.size());
        assertEquals(expectedBrand, result.get(0));
        assertEquals(expectedBrand.getName(), result.get(0).getName());
    }
}
