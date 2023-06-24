package com.shop.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/images/**", "/scripts/**", "/webjars/**", "/api/v1/brands/categories/**")
                .permitAll()

                .requestMatchers("/api/v1/users/**", "/api/v1/settings/**", "/api/v1/countries/**", "/api/v1/states/**")
                .hasAuthority("Admin")

                .requestMatchers("/api/v1/categories/**", "/api/v1/brands/**", "/api/v1/articles/**", "/api/v1/menus/**",
                    "/api/v1/products/new", "/api/v1/products/delete/**", "/api/v1/products/enabled/true/**",
                    "/api/v1/products/enabled/false/**")
                .hasAnyAuthority("Admin", "Editor")

                .requestMatchers("/api/v1/questions/**", "/api/v1/reviews/**")
                .hasAnyAuthority("Admin", "Assistant")

                .requestMatchers("/api/v1/products/edit/**", "/api/v1/products/save", "/api/v1/products/check_name_and_alias",
                    "/api/v1/products/remove_image")
                .hasAnyAuthority("Admin", "Editor", "Salesperson")

                .requestMatchers("/api/v1/customers/**", "/api/v1/shipping/**", "/api/v1/reports/**")
                .hasAnyAuthority("Admin", "Salesperson")

                .requestMatchers("/api/v1/orders/**")
                .hasAnyAuthority("Admin", "Salesperson", "Shipper")

                .requestMatchers("/api/v1/products/detail/**")
                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")

                .anyRequest().authenticated()
            )
            .userDetailsService(userDetailsService())
            .authenticationProvider(authenticationProvider())
            .formLogin(login -> login
                .usernameParameter("email")
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
            )
            .logout(LogoutConfigurer::permitAll)
            .rememberMe(rememberMe -> rememberMe
                .key("ds;flkqha;FEKDJS;lkjdgfasdgdasgHKjKSJAHasijhjDFHkjhlkjFHjkdghasdf")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
            )
            .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }
}
