package com.shop.site.customer;

import com.shop.model.Country;
import com.shop.model.Customer;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
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

    public boolean checkVerificationCode(String code) {
       var customer = customerRepo.findByVerificationCode(code);
       if (customer.isEmpty() || customer.get().isEnabled()) return false;

       customerRepo.enable(customer.get().getId());
       return true;
    }

    @Transactional
    public void save(Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        customer.setEnabled(false);
        customer.setCreatedAt(new Date());
        customer.setVerificationCode(RandomString.make(32));

        customerRepo.save(customer);
    }
}
