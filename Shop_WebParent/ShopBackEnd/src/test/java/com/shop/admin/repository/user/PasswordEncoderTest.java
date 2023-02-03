package com.shop.admin.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    @Test
    public void canEncodePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String password = "password";
        String encoded = encoder.encode(password);

        System.out.println(encoded);

        boolean matches = encoder.matches(password, encoded);

        assertThat(matches).isTrue();
    }

}
