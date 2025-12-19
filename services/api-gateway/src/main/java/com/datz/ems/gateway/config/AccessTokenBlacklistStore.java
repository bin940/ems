package com.datz.ems.gateway.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenBlacklistStore {
  private final StringRedisTemplate redisTemplate;

  public AccessTokenBlacklistStore(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
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
