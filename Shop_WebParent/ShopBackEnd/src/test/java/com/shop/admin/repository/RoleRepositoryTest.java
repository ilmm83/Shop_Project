package com.shop.admin.repository;

import com.shop.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
// @Rollback(false) //-- use this if wanna add new roles into db
class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    public void testCreateFirstRole() {
        Role admin = Role.builder()
                .name("ADMIN")
                .description("Almighty")
                .build();
        Role saved = repository.save(admin);

        assertThat(saved).isEqualTo(admin);
    }

    @Test
    public void testCreateRestRoles() {
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