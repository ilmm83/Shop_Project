package com.shop.admin.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shop.model.User;

public class ShopUserDetailsService implements UserDetailsService{

  @Autowired
  private UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> user  = repository.findByEmail(email);
    if (user.isEmpty()) throw new UsernameNotFoundException("Could not find a user with email: " + email);
    return new ShopUserDetails(user.get());
  }
  
}
