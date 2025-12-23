package com.datz.ems.auth.api.controller;

import com.datz.ems.auth.api.dto.CreateCompanyAdminRequest;
import com.datz.ems.auth.domain.entity.Company;
import com.datz.ems.auth.domain.entity.User;
import com.datz.ems.auth.infra.CompanyRepository;
import com.datz.ems.auth.infra.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/auth/bootstrap")
public class BootstrapController {
  private final CompanyRepository companyRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final String adminSecret;

  public BootstrapController(
      CompanyRepository companyRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      @Value("${auth.bootstrap.admin-secret:}") String adminSecret) {
    this.companyRepository = companyRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.adminSecret = adminSecret;
  }

  @PostMapping("/company-admin")
  public ResponseEntity<?> createCompanyAdmin(
      @RequestHeader(value = "X-Admin-Secret", required = false) String secret,
      @Valid @RequestBody CreateCompanyAdminRequest request) {
    if (adminSecret == null || adminSecret.isBlank()) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(Map.of("message", "Bootstrap disabled"));
    }
    if (secret == null || !adminSecret.equals(secret)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("message", "Unauthorized"));
    }

    String normalizedEmail = request.getAdminEmail().trim().toLowerCase();
    if (userRepository.findByEmail(normalizedEmail).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("message", "Email already exists"));
    }

    Company company = new Company();
    company.setName(request.getCompanyName().trim());
    companyRepository.save(company);

    User admin = new User();
    admin.setEmail(normalizedEmail);
    admin.setPasswordHash(passwordEncoder.encode(request.getAdminPassword()));
    admin.setRole("ADMIN");
    admin.setCompanyId(company.getId());
    userRepository.save(admin);

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "companyId", company.getId().toString(),
        "companyName", company.getName(),
        "adminUserId", admin.getId().toString(),
        "adminEmail", admin.getEmail(),
        "adminRole", admin.getRole()
    ));
  }
}
