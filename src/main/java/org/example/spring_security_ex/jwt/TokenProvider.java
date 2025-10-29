package org.example.spring_security_ex.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.spring_security_ex.entity.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenProvider {
  private final SecretKey secretKey;
  private final long tokenValidityMilliSeconds;

  public TokenProvider(@Value("{jwt.secret}") String secret,
                       @Value("{jwt.expiration-milliseconds}")long tokenValidityMilliSeconds) {
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        SignatureAlgorithm.HS256.getJcaName());
    this.tokenValidityMilliSeconds = tokenValidityMilliSeconds;
  }
  // token build
  public String createJWT(Account account, Long expiredMS) {
    return Jwts.builder()
        .claim("username", account.getUsername())
        .claim("name", account.getName())
        .claim("role", account.getAuthoriy())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiredMS))
        .signWith(secretKey)
        .compact();
  }

}
