package com.shop.site.config;

import com.shop.site.customer.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers("/customer").authenticated()
                .anyRequest().permitAll()
                .and()
                .userDetailsService(userDetailsService())
                .authenticationProvider(authenticationManager())
                .formLogin()
                    .usernameParameter("email")
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/", true)
                .and()
                .logout().permitAll()
                .clearAuthentication(true)
                .and()
                .rememberMe()
                    .key("sad_l12345kfhsak5dlfhsadfhlkjlk")
                    .tokenValiditySeconds(14 * 24 * 60 * 60)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationManager() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }
}
