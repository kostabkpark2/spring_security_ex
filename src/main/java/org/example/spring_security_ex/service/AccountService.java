package org.example.spring_security_ex.service;

import lombok.RequiredArgsConstructor;
import org.example.spring_security_ex.entity.Account;
import org.example.spring_security_ex.repository.AccountRepository;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
  private final AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Account> byUsername = accountRepository.findByUsername(username);
    if(!byUsername.isPresent()) {
      throw new UsernameNotFoundException(username);
    }
    Account account = byUsername.get();
    return User.builder()
        .username(account.getUsername())
        .password(account.getPassword())
        //.roles(account.getAuthoriy())
        .build();
  }
}
