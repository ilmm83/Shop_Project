package com.shop.admin.repository.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.model.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Rollback(true) 
class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    public void canCreateRole() {
        Role admin = Role.builder()
                .name("Admin")
                .description("All might")
                .build();
        Role saved = repository.save(admin);

        assertThat(saved).isEqualTo(admin);
    }

    @Test
    public void canCreateRoles() {
        Role salesperson = Role.builder()
                .name("Salesperson")
                .description("manage product price, customers, shipping, orders and sales report")
                .build();
        Role editor = Role.builder()
                .name("Editor")
                .description("manage categories, brands, products, articles and menus")
                .build();
        Role shipper = Role.builder()
                .name("Shipper")
                .description("view products, view orders and update order status")
                .build();
        Role assistant = Role.builder()
                .name("Assistant")
                .description("manage question and reviews")
                .build();
        List<Role> roles = repository.saveAll(List.of(salesperson, editor, shipper, assistant));

        assertThat(roles).hasSize(4);
    }
}