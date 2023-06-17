package com.shop.site.repository;

import com.common.model.Customer;
import com.shop.site.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryTest {

    @Spy
    CustomerRepository repository;


    @Test
    void canCreateCustomer() {
        // given
        var expectedCustomer = new Customer();

        willReturn(expectedCustomer).given(repository).save(expectedCustomer);

        // when
        var country = repository.save(expectedCustomer);

        // then
        assertEquals(expectedCustomer, country);
    }

    @Test
    void canDeleteCustomer() {
        // given
        var expectedResult = Optional.empty();

        willDoNothing().given(repository).deleteById(1L);
        willReturn(expectedResult).given(repository).findById(1L);

        // when
        repository.deleteById(1L);
        var result = repository.findById(1L);

        // then
        verify(repository, times(1)).deleteById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void canFindByEmail() {
        // given
        var expectedEmail = "test-email";
        var expectedCustomer = new Customer();
        expectedCustomer.setEmail(expectedEmail);
        var expectedResult = Optional.of(expectedCustomer);

        willReturn(expectedResult).given(repository).findByEmail(expectedEmail);

        // when
        var result = repository.findByEmail(expectedEmail);

        // then
        assertFalse(result.isEmpty());
        assertEquals(expectedResult, result);
        assertEquals(expectedCustomer, result.get());
        assertEquals(expectedEmail, result.get().getEmail());
    }

    @Test
    void canFindByVerificationCode() {
    }

    @Test
    void canEnable() {
    }

    @Test
    void canUpdateAuthenticationType() {
    }
}