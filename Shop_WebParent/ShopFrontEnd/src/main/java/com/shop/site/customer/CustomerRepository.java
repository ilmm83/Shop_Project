package com.shop.site.customer;

import com.shop.model.AuthenticationType;
import com.shop.model.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByVerificationCode(String verificationCode);

    Optional<Customer> findByResetPasswordToken(String resetPasswordToken);

    @Modifying
    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
    void enable(Long customerId);

    @Modifying
    @Query("UPDATE Customer c SET c.authenticationType = ?1 WHERE c.id = ?2")
    void updateAuthenticationType(AuthenticationType type, Long id);
}
