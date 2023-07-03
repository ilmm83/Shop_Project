package com.shop.admin.customer;

import com.common.model.Customer;
import com.shop.admin.paging.PagingAndSortingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public void enable(long id, boolean state) {
        repository.enableCustomerById(id, state);
    }

    @Transactional
    public void update(Customer formCustomer) {
        var DBCustomer = findById(formCustomer.getId());

        if (!formCustomer.getPassword().isEmpty()) {
            formCustomer.setPassword(encoder.encode(formCustomer.getPassword()));
        } else {
            formCustomer.setPassword(DBCustomer.getPassword());
        }

        formCustomer.setEnabled(DBCustomer.isEnabled());
        formCustomer.setCreatedAt(DBCustomer.getCreatedAt());
        formCustomer.setVerificationCode(DBCustomer.getVerificationCode());
        formCustomer.setAuthenticationType(DBCustomer.getAuthenticationType());
        formCustomer.setResetPasswordToken(DBCustomer.getResetPasswordToken());

        repository.save(formCustomer);
    }

    @Transactional
    public void deleteById(long id) {
        repository.countById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Could not found a customer with the ID: " + id));

        repository.deleteById(id);
    }
}
