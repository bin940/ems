package com.datz.ems.auth.config;

import com.datz.ems.auth.domain.Company;
import com.datz.ems.auth.domain.User;
import com.datz.ems.auth.infra.CompanyRepository;
import com.datz.ems.auth.infra.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
  @Bean
  CommandLineRunner seed(CompanyRepository companyRepository, UserRepository userRepository, PasswordEncoder encoder) {
    return args -> {
      if (userRepository.count() == 0) {
        Company company = new Company();
        company.setName("Demo Company");
        companyRepository.save(company);

        User admin = new User();
        admin.setEmail("admin@demo.local");
        admin.setPasswordHash(encoder.encode("admin1234"));
        admin.setRole("ADMIN");
        admin.setCompanyId(company.getId());
        userRepository.save(admin);
      }
    };
  }
}
