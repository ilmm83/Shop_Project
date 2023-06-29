package com.shop.admin.customer;

import com.common.model.Customer;
import com.shop.admin.paging.SearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends SearchRepository<Customer, Long> {

    @Modifying
    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    void enableCustomerById(long id, boolean state);

    Optional<Customer> findById(long id);

    Optional<Customer> findByEmail(String email);

    Optional<Boolean> countById(long id);

    void save(Customer customer);

    void deleteById(long id);
}
