package com.datz.ems.auth.api.controller;

import com.datz.ems.auth.api.dto.CreateUserRequest;
import com.datz.ems.auth.domain.entity.User;
import com.datz.ems.auth.infra.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/users")
public class UserAdminController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserAdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request, Authentication authentication) {
    if (authentication == null || !(authentication.getDetails() instanceof Claims claims)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    String companyId = claims.get("companyId", String.class);
    if (companyId == null || companyId.isBlank()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
    }

    String normalizedEmail = request.getEmail().trim().toLowerCase();
    if (userRepository.findByEmail(normalizedEmail).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
    }

    User user = new User();
    user.setEmail(normalizedEmail);
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setRole(request.getRole().trim().toUpperCase());
    user.setCompanyId(UUID.fromString(companyId));
    userRepository.save(user);

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "id", user.getId().toString(),
        "email", user.getEmail(),
        "role", user.getRole(),
        "companyId", user.getCompanyId().toString()
    ));
  }
}
