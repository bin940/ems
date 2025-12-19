package com.datz.ems.gateway.config;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
  private final JwtService jwtService;
  private final AccessTokenBlacklistStore accessTokenBlacklistStore;

  public JwtAuthFilter(JwtService jwtService, AccessTokenBlacklistStore accessTokenBlacklistStore) {
    this.jwtService = jwtService;
    this.accessTokenBlacklistStore = accessTokenBlacklistStore;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    if (path.startsWith("/auth/login")
        || path.startsWith("/auth/refresh")
        || path.startsWith("/auth/logout")
        || path.startsWith("/actuator/health")) {
      return chain.filter(exchange);
    }

    String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    String token = header.substring(7);
    try {
      Claims claims = jwtService.parse(token);
      String type = claims.get("type", String.class);
      if (!"access".equals(type)) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }

      String tokenId = claims.getId();
      if (tokenId == null || accessTokenBlacklistStore.isBlacklisted(tokenId)) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }

      var mutatedRequest = exchange.getRequest().mutate()
          .header("X-Company-Id", claims.get("companyId", String.class))
          .header("X-User-Id", claims.getSubject())
          .header("X-Role", claims.get("role", String.class))
          .build();
      var mutatedExchange = exchange.mutate().request(mutatedRequest).build();
      return chain.filter(mutatedExchange);
    } catch (Exception ex) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
