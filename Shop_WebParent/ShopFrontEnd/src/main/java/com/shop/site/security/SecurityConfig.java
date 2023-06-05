package com.shop.site.security;

import com.shop.site.customer.CustomerUserDetailsService;
import com.shop.site.security.handler.DatabaseLoginSuccessHandler;
import com.shop.site.security.handler.OAuth2LoginSuccessHandler;
import com.shop.site.security.oauth2.CustomerOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomerOAuth2UserService auth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/customer").authenticated()
                .anyRequest().permitAll()
            )
            .userDetailsService(userDetailsService())
            .authenticationProvider(authenticationProvider())
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .successHandler(databaseLoginSuccessHandler)
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(endpointConfig -> endpointConfig.userService(auth2UserService))
                .successHandler(auth2LoginSuccessHandler)
                .failureHandler(authenticationFailureHandler)
            )
            .logout(logout -> logout
                .clearAuthentication(true)
                .permitAll()
            )
            .rememberMe(rememberMe -> rememberMe
                .key("sad_l12345kfhsak5dlfhsadfhlkjlk")
                .tokenValiditySeconds(14 * 24 * 60 * 60)
            )
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
    public DaoAuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }
}
