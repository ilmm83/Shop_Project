package com.shop.site.customer;

import com.common.model.AuthenticationType;
import com.common.model.Country;
import com.common.model.Customer;
import com.shop.site.country.CountryNotFoundException;
import com.shop.site.country.CountryRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CountryRepository countryRepository;
    private final PasswordEncoder encoder;


    public List<Country> listAllCountries() {
        return (List<Country>) countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Long id, String email) {
        var customer = customerRepository.findByEmail(email);

        if (customer.isEmpty()) return true;
        else if (id == null) return false;
        else return Objects.equals(customer.get().getId(), id);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new CountryNotFoundException("Could not find a customer with the E-mail: " + email));
    }

    public boolean checkResetPasswordToken(String token) {
        return customerRepository.findByResetPasswordToken(token)
            .map(customer -> customer.getResetPasswordToken().equals(token)).orElse(false);
    }

    @Transactional
    public String updateResetPasswordToken(String email) {
        var found = findByEmail(email);
        var token = RandomString.make(30);

        found.setResetPasswordToken(token);
        customerRepository.save(found);

        return token;
    }

    @Transactional
    public boolean checkVerificationCode(String code) {
        var customer = customerRepository.findByVerificationCode(code);

        if (customer.isEmpty() || customer.get().isEnabled()) return false;
        customerRepository.enable(customer.get().getId());

        return true;
    }

    @Transactional
    public void update(Customer formCustomer) {
        var DBCustomer = findByEmail(formCustomer.getEmail());

        if (DBCustomer.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (!formCustomer.getPassword().isEmpty()) {
                formCustomer.setPassword(encoder.encode(formCustomer.getPassword()));
            } else {
                formCustomer.setPassword(DBCustomer.getPassword());
            }
        } else {
            formCustomer.setPassword(DBCustomer.getPassword());
        }

        formCustomer.setEnabled(DBCustomer.isEnabled());
        formCustomer.setCreatedAt(DBCustomer.getCreatedAt());
        formCustomer.setVerificationCode(DBCustomer.getVerificationCode());
        formCustomer.setAuthenticationType(DBCustomer.getAuthenticationType());
        formCustomer.setResetPasswordToken(DBCustomer.getResetPasswordToken());

        customerRepository.save(formCustomer);
    }

    @Transactional
    public void save(Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        customer.setEnabled(false);
        customer.setCreatedAt(new Date());
        customer.setVerificationCode(RandomString.make(32));
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        customerRepository.save(customer);
    }

    @Transactional
    public void updatePassword(String token, String password) {
        customerRepository.findByResetPasswordToken(token)
            .ifPresent(found -> {
                found.setResetPasswordToken(null);
                found.setPassword(encoder.encode(password));
                found.setCreatedAt(new Date());

                customerRepository.save(found);
            });
    }
}
