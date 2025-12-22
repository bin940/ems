package com.datz.ems.auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCompanyAdminRequest {
  @NotBlank
  @Size(max = 200)
  private String companyName;

  @NotBlank
  @Email
  private String adminEmail;

  @NotBlank
  @Size(min = 8, max = 100)
  private String adminPassword;

  public String getCompanyName() {
    return companyName;
  }

  public String getAdminEmail() {
    return adminEmail;
  }

  public String getAdminPassword() {
    return adminPassword;
  }
}
