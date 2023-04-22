package com.shop.site.repository.customer;

import com.shop.model.AuthenticationType;
import com.shop.model.Country;
import com.shop.model.Customer;
import com.shop.site.BaseTest;
import com.shop.site.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Rollback(value = false)
@AutoConfigureTestDatabase(replace = NONE)
class CustomerRepositoryTest extends BaseTest {

    @Autowired
    private CustomerRepository repository;

    private Customer customer;


    @BeforeEach
    void setup() {
        var countryId = 21;
        var country = em.find(Country.class, countryId);

        customer = new Customer();
        customer.setId(6L);
        customer.setCountry(country);
        customer.setFirstName("David");
        customer.setLastName("Fountaine");
        customer.setPassword("password123");
        customer.setEmail("david.s.fountaine@gmail.com");
        customer.setPhoneNumber("312-462-7518");
        customer.setAddressLine1("1927 West Drive");
        customer.setCity("Sacramento");
        customer.setState("California");
        customer.setPostalCode("95867");
        customer.setCreatedAt(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
    }

    @Test
    void canCreateCustomer() {
        var saved = repository.save(customer);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    void canDeleteCustomer() {
        repository.deleteById(customer.getId());

        var found = em.find(Customer.class, customer.getId());
        assertThat(found).isNull();
    }

    @Test
    void canFindByEmail() {
        var found = repository.findByEmail(customer.getEmail());

        assertThat(found.isEmpty()).isFalse();
        assertThat(found.get().getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canFindByVerificationCode() {
        var found = repository.findByVerificationCode(customer.getVerificationCode());

        assertThat(found.isEmpty()).isFalse();
        assertThat(found.get().getVerificationCode()).isEqualTo(customer.getVerificationCode());
    }

    @Test
    void canEnable() {
        customer.setEnabled(true);
        var saved = repository.save(customer);

        assertThat(saved.isEnabled()).isTrue();
    }

    @Test
    void canUpdateAuthenticationType() {
        repository.updateAuthenticationType(AuthenticationType.DATABASE, customer.getId());

        var found = em.find(Customer.class, customer.getId());

        assertThat(found.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
    }
}