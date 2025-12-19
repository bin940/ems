package com.datz.ems.auth.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datz.ems.auth.domain.entity.Company;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
}
