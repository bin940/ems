package com.datz.ems.analytics.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class AuthContext {
  private AuthContext() {
  }

  public static Claims claims() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getDetails() instanceof Claims claims) {
      return claims;
    }
    return null;
  }

  public static UUID companyId() {
    Claims claims = claims();
    if (claims == null) {
      return null;
    }
    String companyId = claims.get("companyId", String.class);
    return companyId == null ? null : UUID.fromString(companyId);
  }
}
