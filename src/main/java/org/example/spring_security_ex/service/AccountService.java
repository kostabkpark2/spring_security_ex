package org.example.spring_security_ex.service;

import lombok.RequiredArgsConstructor;
import org.example.spring_security_ex.entity.Account;
import org.example.spring_security_ex.entity.LoginUser;
import org.example.spring_security_ex.entity.Role;
import org.example.spring_security_ex.repository.AccountRepository;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    return new LoginUser(
        account.getUsername(),
        account.getPassword(),
        getAuthorityList(account.getAuthoriy()),
        account.getName(),
        account.getPhone());
  }

  // DB 에는 원자값만 저장할 수 있기 때문에
  // 주어진 권한 => 권한정보 컬렉션으로 변환
  // admin 의 경우 user 와 admin 권한을 두 개 collection 으로 가질 수 있게 변환
  // authority 설정 메서드
  private List<GrantedAuthority> getAuthorityList(Role role) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role.name())); // user => USER , admin => ADMIN
    if (role == Role.ADMIN) {
      authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
    }
    return authorities;
  }

}
