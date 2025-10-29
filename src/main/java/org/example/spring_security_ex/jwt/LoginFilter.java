package org.example.spring_security_ex.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    // 인증 시도하는 정보 가져와서 로그 찍어서 확인해보기 - 확인 후 삭제하기
    String username = super.obtainUsername(request);
    String password = super.obtainPassword(request);
    // 인증 시도하는 정보 가져와서 로그 찍어서 확인해보기 - 확인 후 삭제하기
    log.info("Attempting to authenticate username : {} ", username);
    log.info("Attempting to authenticate password : {} ", password);
    // 상위 AuthenticationManager에게 인증 책임을 위임 ? 하는 경우인지 확인
    return authenticationManager.authenticate(null);
  }
}
