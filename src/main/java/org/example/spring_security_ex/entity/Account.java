package org.example.spring_security_ex.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String username;
  private String password;
  @Enumerated(EnumType.STRING) // 숫자가 아닌 문자열로 저장되게
  private Role authoriy; // ADMIN , USER
  private String name;
  private String phone;
}
