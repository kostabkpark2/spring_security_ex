package org.example.spring_security_ex.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class Authentication extends User {
  private int userid;
  private String name;
  private String phoneNumber;

  public Authentication(String username, String password, Collection<? extends GrantedAuthority> authorities, String name, String phoneNumber) {
    super(username, password, authorities);
    this.name = name;
    this.phoneNumber = phoneNumber;
  }
}
