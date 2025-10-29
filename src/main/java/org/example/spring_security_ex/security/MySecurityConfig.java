package org.example.spring_security_ex.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.spring_security_ex.jwt.JwtFilter;
import org.example.spring_security_ex.jwt.LoginFilter;
import org.example.spring_security_ex.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MySecurityConfig {
  private final AuthenticationConfiguration authenticationConfiguration;
  private final TokenProvider tokenProvider;

  // AuthenticationManager Bean 으로 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  // passwordEncoder Bean 으로 등록
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // securityFilterChain bean 으로 등록
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // CORS 설정
    http.cors((corsCustomizer) ->
      corsCustomizer.configurationSource(new CorsConfigurationSource() {
        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
          CorsConfiguration c = new CorsConfiguration();
          c.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
          c.setAllowedMethods(Collections.singletonList("*"));
          c.setAllowCredentials(true);
          c.setAllowedHeaders(Collections.singletonList("*"));
          c.setMaxAge(3600L);
          c.setExposedHeaders(Collections.singletonList("Authorization"));

          return c;
        }
      }
      ));


    http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/main").permitAll()
                .anyRequest().authenticated());


    http.csrf(csrf -> csrf.disable());
    http.formLogin(auth -> auth.disable());
    http.httpBasic(auth -> auth.disable());
    http.sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(new JwtFilter(tokenProvider), LoginFilter.class);
    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), tokenProvider),
          UsernamePasswordAuthenticationFilter.class);

    return http.build();
  };
}
