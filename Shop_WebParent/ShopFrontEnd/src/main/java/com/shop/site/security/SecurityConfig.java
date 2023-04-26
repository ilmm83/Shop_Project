package com.shop.site.security;

import com.shop.site.customer.CustomerUserDetailsService;
import com.shop.site.security.handler.DatabaseLoginSuccessHandler;
import com.shop.site.security.handler.OAuth2LoginSuccessHandler;
import com.shop.site.security.oauth2.CustomerOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationFailureHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
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
                .authorizeHttpRequests()
                .requestMatchers("/customer").authenticated()
                .anyRequest().permitAll()
                .and()
                .userDetailsService(userDetailsService())
                .authenticationProvider(authenticationProvider())
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .successHandler(databaseLoginSuccessHandler)
                    .permitAll()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                        .userService(auth2UserService)
                    .and()
                    .successHandler(auth2LoginSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                    .clearAuthentication(true)
                    .permitAll()
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
    public DaoAuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }
}
