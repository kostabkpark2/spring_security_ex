package org.example.spring_security_ex.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring_security_ex.dto.LoginForm;
import org.example.spring_security_ex.entity.Account;
import org.example.spring_security_ex.entity.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    // 인증 시도하는 정보 가져와서 로그 찍어서 확인해보기 - 확인 후 삭제하기
// 테스트시 post 메서드로 localhost:8080/login 으로 접속
// 단, parameter 에서 바로 꺼내려면 body -> x-www-form-urlencoded 로
// 로그인 정보 입력하고 아래 둘 중의 하나의 코드 사용하면 됨.
//    String username = request.getParameter("username");
//    String password = request.getParameter("password");

    String username = super.obtainUsername(request);
    String password = super.obtainPassword(request);

// body -> raw -> json 으로 입력하면 자바 객체로 변환해줘야 하기 때문에 아래와 같이 코드 변경해야 함
//    try {
      // JSON 데이터 읽기
//      ObjectMapper objectMapper = new ObjectMapper();
//      LoginForm loginRequest = objectMapper.readValue(request.getInputStream(), LoginForm.class);
//
//      String username = loginRequest.getUsername();
//      String password = loginRequest.getPassword();

      log.info("Attempting to authenticate username : {}", username);
      log.info("Attempting to authenticate password : {}", password);

      // 인증 토큰 생성
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(username, password);
      log.info("authenticationToken : {}", authenticationToken);

      // AuthenticationManager 에게 인증 위임
      Authentication authentication = authenticationManager.authenticate(authenticationToken);

      log.info("authentication : {} ", authentication);
      return authentication;

//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

    String username = customUserDetails.getUsername();
    Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();

    String role = auth.getAuthority();
    Account account = new Account();
    account.setUsername(username);
    // 문자열을 enum 값으로 변환
    account.setAuthoriy(Role.valueOf(role));
    String token = tokenProvider.createJWT(account, 60*10*1000L);

//    // 스프링 시큐리티 인증 토큰 생성  ?????
//    Authentication authToken  = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//    // 세션에 사용자 정보 등록 ???????
//    SecurityContextHolder.getContext().setAuthentication(authToken);

    response.addHeader("Authorization", "Bearer " + token);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    response.setStatus(401); // unauthorized
  }
}
