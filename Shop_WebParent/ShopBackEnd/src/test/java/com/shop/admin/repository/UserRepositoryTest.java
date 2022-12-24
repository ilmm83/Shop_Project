package com.shop.admin.repository;

import com.shop.model.Role;
import com.shop.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

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
        roleRepository.deleteAll();
    }

    @Test
    public void testUserCreation() {
        Role editor = Role.builder()
                .name("Editor")
                .description("manage categories, brands, products, articles and menus")
                .build();
        Role assistant = Role.builder()
                .name("Assistant")
                .description("manage question and reviews")
                .build();

        Set<Role> roles = new HashSet<>();
        roles.add(editor);
        roles.add(assistant);

        User user = User.builder()
                .email("user2@gmail.com")
                .roles(roles)
                .firstName("first name 2")
                .lastName("last name 2")
                .enabled(true)
                .password("password2")
                .photos("photo")
                .build();

        roleRepository.save(editor);
        roleRepository.save(assistant);
        User saved = userRepository.save(user);

        assertThat(saved).isEqualTo(user);
    }
}