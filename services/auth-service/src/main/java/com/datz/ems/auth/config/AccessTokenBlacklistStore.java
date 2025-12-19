package com.datz.ems.auth.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class AccessTokenBlacklistStore {
  private final StringRedisTemplate redisTemplate;

  public AccessTokenBlacklistStore(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void blacklist(String tokenId, Instant expiresAt) {
    if (tokenId == null || tokenId.isBlank() || expiresAt == null) {
      return;
    }

    Duration ttl = Duration.between(Instant.now(), expiresAt);
    if (ttl.isNegative() || ttl.isZero()) {
      return;
    }

    redisTemplate.opsForValue().set(key(tokenId), "1", ttl);
  }

  public boolean isBlacklisted(String tokenId) {
    if (tokenId == null || tokenId.isBlank()) {
      return false;
    }
    return Boolean.TRUE.equals(redisTemplate.hasKey(key(tokenId)));
  }

  private String key(String tokenId) {
    return "access:blacklist:" + tokenId;
  }
}
