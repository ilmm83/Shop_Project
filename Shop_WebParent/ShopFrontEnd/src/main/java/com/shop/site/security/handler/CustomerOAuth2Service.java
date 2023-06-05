package com.shop.site.security.handler;


import com.shop.model.AuthenticationType;
import com.shop.model.Customer;
import com.shop.site.country.CountryNotFoundException;
import com.shop.site.country.CountryRepository;
import com.shop.site.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerOAuth2Service {

    private final CustomerRepository customerRepository;
    private final CountryRepository countryRepo;


    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional
    public void updateAuthenticationType(AuthenticationType type, Long id) {
        customerRepository.updateAuthenticationType(type, id);
    }

    @Transactional
    public void addNewCustomerUponOAuthLogin(String name, String email, String code) {
        var customer = new Customer();
        var country = countryRepo.findByCode(code)
                .orElseThrow(() -> new CountryNotFoundException("Could not find a country with the code: " + code));

        setName(name, customer);
        customer.setCountry(country);
        customer.setEmail(email);
        customer.setCreatedAt(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setEnabled(true);
        customer.setPassword("");
        customer.setPhoneNumber("");
        customer.setAddressLine1("");
        customer.setPostalCode("");
        customer.setState("");
        customer.setCity("");

        customerRepository.save(customer);
    }

    private void setName(String name, Customer customer) {
        var arr = name.split(" ");
        if (arr.length < 2) {
            customer.setFirstName(name);
            customer.setLastName("");
        } else {
            customer.setFirstName(arr[0]);
            customer.setLastName(arr[1]);
        }
    }
}
