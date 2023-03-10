package com.shop.admin.repository.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shop.model.User;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback(true)
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
        Long id = 10l;
        var counted = userRepository.countById(id).get();

        assertThat(counted).isPositive();
    }

    @Test
    public void canCreateUser() {
        var editor = roleRepository.findById(3L).get();
        var assistant = roleRepository.findById(5L).get();

        var roles = Set.of(editor, assistant);

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

        var assistant = roleRepository.findById(5L).get();

        User user = User.builder()
                .email("user2@gmail.com")
                .roles(Set.of(assistant))
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