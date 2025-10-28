package org.example.spring_security_ex.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("user")
        .password("user1111")
        .roles("USER")
        .build();
    UserDetails admin = User.withDefaultPasswordEncoder()
        .username("admin")
        .password("admin1111")
        .roles("ADMIN","USER")
        .build();
    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize
                  .requestMatchers("/index.html").permitAll()
                  .anyRequest().authenticated())
        .formLogin(Customizer.withDefaults());
    DefaultSecurityFilterChain chain = http.build();
    chain.getFilters().forEach(System.out::println);
    return chain;
  };
}
