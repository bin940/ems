package com.datz.ems.auth.api;

import com.datz.ems.auth.config.AccessTokenBlacklistStore;
import com.datz.ems.auth.config.JwtService;
import com.datz.ems.auth.config.RefreshTokenStore;
import com.datz.ems.auth.domain.User;
import com.datz.ems.auth.infra.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final RefreshTokenStore refreshTokenStore;
  private final AccessTokenBlacklistStore accessTokenBlacklistStore;

  public AuthController(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      RefreshTokenStore refreshTokenStore,
      AccessTokenBlacklistStore accessTokenBlacklistStore) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.refreshTokenStore = refreshTokenStore;
    this.accessTokenBlacklistStore = accessTokenBlacklistStore;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail()).orElse(null);
    if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("message", "Invalid credentials"));
    }

    String accessToken = jwtService.generateAccessToken(
        user.getId().toString(),
        user.getCompanyId().toString(),
        user.getRole(),
        user.getEmail()
    );

    String refreshToken = jwtService.generateRefreshToken(
        user.getId().toString(),
        user.getCompanyId().toString(),
        user.getRole(),
        user.getEmail()
    );

    refreshTokenStore.store(refreshToken, user.getId().toString());

    return ResponseEntity.ok(Map.of(
        "accessToken", accessToken,
        "refreshToken", refreshToken,
        "userId", user.getId().toString(),
        "companyId", user.getCompanyId().toString(),
        "role", user.getRole()
    ));
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest request) {
    try {
      var claims = jwtService.parse(request.getRefreshToken());
      if (!"refresh".equals(claims.get("type", String.class))) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Invalid refresh token"));
      }

      if (!refreshTokenStore.exists(request.getRefreshToken())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Refresh token revoked"));
      }

      String accessToken = jwtService.generateAccessToken(
          claims.getSubject(),
          claims.get("companyId", String.class),
          claims.get("role", String.class),
          claims.get("email", String.class)
      );

      String newRefreshToken = jwtService.generateRefreshToken(
          claims.getSubject(),
          claims.get("companyId", String.class),
          claims.get("role", String.class),
          claims.get("email", String.class)
      );

      refreshTokenStore.delete(request.getRefreshToken());
      refreshTokenStore.store(newRefreshToken, claims.getSubject());

      return ResponseEntity.ok(new RefreshResponse(accessToken, newRefreshToken));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("message", "Invalid refresh token"));
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(
      @Valid @RequestBody RefreshRequest request,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
    refreshTokenStore.delete(request.getRefreshToken());

    if (authorization != null && authorization.startsWith("Bearer ")) {
      String accessToken = authorization.substring(7);
      try {
        var accessClaims = jwtService.parse(accessToken);
        if ("access".equals(accessClaims.get("type", String.class))) {
          accessTokenBlacklistStore.blacklist(accessClaims.getId(), accessClaims.getExpiration().toInstant());
        }
      } catch (Exception ignored) {
      }
    }

    return ResponseEntity.ok(Map.of("message", "Logged out"));
  }
}
