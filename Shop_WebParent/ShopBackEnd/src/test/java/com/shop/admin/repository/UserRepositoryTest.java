package com.shop.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shop.model.Role;
import com.shop.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void beforeEach() {
        // userRepository.deleteAll();
        // roleRepository.deleteAll();
    }

    @Test
    public void canChangeUserStatus() {
    
    userRepository.updateEnabledStatus(1L, true);
    
    }

    @Test
    public void canCountUserId() {
        Long id = 1l;
        Long counted = userRepository.countById(id);

        assertThat(counted).isEqualTo(id);
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

    @Test
    public void canGetUserByEmail() {

        User user = User.builder()
                .email("user2@gmail.com")
                .roles(null)
                .firstName("first name 2")
                .lastName("last name 2")
                .enabled(true)
                .password("password2")
                .photos("photo")
                .build();
        userRepository.save(user);

        System.out.println(userRepository.findByEmail(user.getEmail()).get());
    }
}