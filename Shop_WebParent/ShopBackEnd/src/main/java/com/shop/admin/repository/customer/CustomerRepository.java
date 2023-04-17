package com.shop.admin.repository.customer;

import com.shop.model.Customer;
import com.shop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.id, ' ', c.firstName, ' ', c.lastName, ' ', c.email) LIKE %?1%")
    Page<Customer> findAll(String keyword, Pageable pageable);

    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    void enableCustomerById(long id, boolean state);

    Optional<Customer> findById(long id);

    Optional<Customer> findByEmail(String email);

    void save(Customer customer);

}
