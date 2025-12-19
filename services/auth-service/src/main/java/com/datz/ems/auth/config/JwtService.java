package com.datz.ems.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtService {
  private final SecretKey key;
  private final String issuer;
  private final long accessTokenMinutes;
  private final long refreshTokenDays;

  public JwtService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.issuer}") String issuer,
      @Value("${jwt.access-token-minutes}") long accessTokenMinutes,
      @Value("${jwt.refresh-token-days}") long refreshTokenDays) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.issuer = issuer;
    this.accessTokenMinutes = accessTokenMinutes;
    this.refreshTokenDays = refreshTokenDays;
  }

  public String generateAccessToken(String userId, String companyId, String role, String email) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(accessTokenMinutes * 60);

    return Jwts.builder()
        .issuer(issuer)
        .subject(userId)
        .id(UUID.randomUUID().toString())
        .claims(Map.of(
            "companyId", companyId,
            "role", role,
            "email", email,
            "type", "access"
        ))
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .signWith(key)
        .compact();
  }

  public String generateRefreshToken(String userId, String companyId, String role, String email) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(refreshTokenDays * 24 * 60 * 60);

    return Jwts.builder()
        .issuer(issuer)
        .subject(userId)
        .claims(Map.of(
            "companyId", companyId,
            "role", role,
            "email", email,
            "type", "refresh"
        ))
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .signWith(key)
        .compact();
  }

  public Claims parse(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
