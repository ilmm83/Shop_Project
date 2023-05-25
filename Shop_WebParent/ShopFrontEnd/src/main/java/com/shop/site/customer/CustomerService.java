package com.shop.site.customer;

import com.shop.model.AuthenticationType;
import com.shop.model.Country;
import com.shop.model.Customer;
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

    private final CustomerRepository customerRepo;
    private final CountryRepository countryRepo;
    private final PasswordEncoder encoder;

    public List<Country> listAllCountries() {
        return (List<Country>) countryRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Long id, String email) {
        var customer = customerRepo.findByEmail(email);
        if (customer.isEmpty()) return true;
        else if (id == null) return false;
        else return Objects.equals(customer.get().getId(), id);
    }

    public Customer findByEmail(String email) {
        return customerRepo.findByEmail(email)
                .orElseThrow(() -> new CountryNotFoundException("Could not find a customer with the E-mail: " + email));
    }

    public boolean checkResetPasswordToken(String token) {
        return customerRepo.findByResetPasswordToken(token)
                .map(customer -> customer.getResetPasswordToken().equals(token)).orElse(false);
    }

    @Transactional
    public String updateResetPasswordToken(String email) {
        var found = findByEmail(email);
        var token = RandomString.make(30);

        found.setResetPasswordToken(token);
        customerRepo.save(found);

        return token;
    }

    @Transactional
    public boolean checkVerificationCode(String code) {
        var customer = customerRepo.findByVerificationCode(code);
        if (customer.isEmpty() || customer.get().isEnabled()) return false;

        customerRepo.enable(customer.get().getId());
        return true;
    }

    @Transactional
    public void update(Customer customer) {
        if (customer.getPassword() == null) customer.setPassword("");

        customer.setPassword(encoder.encode(customer.getPassword()));
        customer.setCreatedAt(new Date());

        customerRepo.save(customer);
    }

    @Transactional
    public void save(Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        customer.setEnabled(false);
        customer.setCreatedAt(new Date());
        customer.setVerificationCode(RandomString.make(32));
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        customerRepo.save(customer);
    }

    @Transactional
    public void updateCustomerPassword(String token, String password) {
        customerRepo.findByResetPasswordToken(token)
                .ifPresent(found -> {
                    found.setResetPasswordToken("");
                    found.setPassword(encoder.encode(password));
                    found.setCreatedAt(new Date());

                    customerRepo.save(found);
                });
    }
}
