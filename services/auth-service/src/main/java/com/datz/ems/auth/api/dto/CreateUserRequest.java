package com.datz.ems.auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 8, max = 100)
  private String password;

  @NotBlank
  @Pattern(regexp = "(?i)ADMIN|OPERATOR|VIEWER")
  private String role;

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getRole() {
    return role;
  }
}
