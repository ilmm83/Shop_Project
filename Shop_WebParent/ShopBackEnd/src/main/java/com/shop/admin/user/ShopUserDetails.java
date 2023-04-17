package com.shop.admin.user;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shop.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShopUserDetails implements UserDetails {

  private User user;
  private static final long serialVersionUID = 1l;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.getEnabled();
  }

  public String getFullName() {
    return user.getFullName(); 
  }

  public void setFirstName(String firstName) {
    user.setFirstName(firstName);
  }

  public void setLastName(String lastName) {
    user.setLastName(lastName);
  }

  public boolean hasRole(String roleName) {
      return user.hasRole(roleName);
  }
}