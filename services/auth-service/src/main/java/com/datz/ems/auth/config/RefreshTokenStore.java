package com.datz.ems.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenStore {
  private final StringRedisTemplate redisTemplate;
  private final Duration ttl;

  public RefreshTokenStore(
      StringRedisTemplate redisTemplate,
      @Value("${jwt.refresh-token-days}") long refreshTokenDays) {
    this.redisTemplate = redisTemplate;
    this.ttl = Duration.ofDays(refreshTokenDays);
  }

  public void store(String token, String userId) {
    redisTemplate.opsForValue().set(key(token), userId, ttl);
  }

  public boolean exists(String token) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key(token)));
  }

  public void delete(String token) {
    redisTemplate.delete(key(token));
  }

  private String key(String token) {
    return "refresh:" + token;
  }
}
