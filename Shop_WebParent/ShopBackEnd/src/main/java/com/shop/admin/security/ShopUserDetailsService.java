package com.shop.admin.security;

import com.shop.admin.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new ShopUserDetails(repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Could not find a user with email: " + email)));
    }

}
