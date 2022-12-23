package com.shop.admin.repository;

import com.shop.model.Role;
import com.shop.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void afterEach() {
        userRepository.deleteAll();
        ;
        roleRepository.deleteAll();
    }

    @Test
    public void testUserCreation() {
        Role admin = Role.builder()
                .name("Admin")
                .description("All mighty")
                .build();

        Set<Role> roles = new HashSet<>();
        roles.add(admin);

        User user = User.builder()
                .email("user1@gmail.com")
                .roles(roles)
                .firstName("first name 1")
                .lastName("last name 1")
                .enabled(true)
                .password("password1")
                .photos("photo")
                .build();

        roleRepository.save(admin);
        User saved = userRepository.save(user);

        assertThat(saved).isEqualTo(user);
        assertThat(saved.getRoles().contains(admin)).isTrue();
    }
}