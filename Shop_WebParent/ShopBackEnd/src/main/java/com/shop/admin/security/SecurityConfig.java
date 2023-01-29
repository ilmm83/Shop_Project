package com.shop.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shop.admin.security.user.ShopUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                    .requestMatchers("/images/**", "/scripts/**", "/webjars/**").permitAll()
                    .requestMatchers("/api/v1/users").hasAuthority("Admin")
                    .anyRequest().authenticated()    
                .and()
                .userDetailsService(userDetailsService())
                .authenticationProvider(authenticatonProvider())
                .formLogin()
                    .usernameParameter("email")
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/", true)
                .and()
                .logout().permitAll()
                .and()
                .rememberMe()
                    .key("ds;flkqha;FEKDJS;lkjdgfasdgdasgHKjKSJAHasijhjDFHkjhlkjFHjkdghasdf")
                    .tokenValiditySeconds(7 * 24 * 60 * 60)
                .and()
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
    public DaoAuthenticationProvider authenticatonProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }
}
