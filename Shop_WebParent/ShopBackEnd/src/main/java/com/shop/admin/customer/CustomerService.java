package com.shop.admin.customer;

import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final PasswordEncoder encoder;
    public static final int PAGE_SIZE = 10;


    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.searchEntities(pageNum, PAGE_SIZE, repository);
    }

    public List<Customer> findAllUsersSortedByFirstName() {
        return (List<Customer>) repository.findAll(Sort.by("id").ascending());
    }

    public boolean isEmailUnique(Long id, String email) {
        var found = repository.findByEmail(email);
        if (found.isEmpty()) return true;
        else if (id == null) return false;
        else return Objects.equals(found.get().getId(), id);
    }

    public Customer findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Could not found a customer with the ID: " + id));
    }

    @Transactional
    public void enableCustomer(long id, boolean state) {
        repository.enableCustomerById(id, state);
    }

    @Transactional
    public void updateCustomer(Customer customer) {
        var found = findById(customer.getId());

        if (!customer.getPassword().isBlank())
            found.setPassword(encoder.encode(customer.getPassword()));
        found.setFirstName(customer.getFirstName());
        found.setLastName(customer.getLastName());
        found.setEmail(customer.getEmail());
        found.setPhoneNumber(customer.getPhoneNumber());
        found.setAddressLine1(customer.getAddressLine1());
        found.setAddressLine2(customer.getAddressLine2());
        found.setCity(customer.getCity());
        found.setCountry(customer.getCountry());
        found.setState(customer.getState());
        found.setVerificationCode(customer.getVerificationCode());
        found.setCreatedAt(customer.getCreatedAt());
        found.setEnabled(customer.isEnabled());

        repository.save(found);
    }

    @Transactional
    public void deleteById(long id) {
        repository.countById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Could not found a customer with the ID: " + id));
        repository.deleteById(id);
    }
}
